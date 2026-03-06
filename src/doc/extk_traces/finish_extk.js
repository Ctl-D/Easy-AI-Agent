const fs = require('fs');
const path = require('path');
const crypto = require('crypto');

// 1. 获取当前时间并做系统兼容格式化
const now = new Date();
const dateString = now.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' }).replace(/\//g, '-');
const timeString = now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' });

// 2. 接收外部传入的总结参数 (由 AI 分析后传入)
// 例: node finish_extk.js "提取了多模态概念..." "为xxx补充了注释..."
const args = process.argv.slice(2);
const addedConcepts = args[0] || "无新增核心概念";
const fileChanges = args[1] || "无文件与代码变更";

// 3. 构建并写入日志
const logDir = path.join(__dirname, '..', 'extract_log');
if (!fs.existsSync(logDir)) {
    fs.mkdirSync(logDir, { recursive: true });
}

const logFile = path.join(logDir, `${dateString}.md`);
let logContent = '';
if (!fs.existsSync(logFile)) {
    logContent += `# ${dateString} 知识提取变更日志\n\n`;
} else {
    logContent = fs.readFileSync(logFile, 'utf8');
}

const newEntry = `## [${timeString}] 执行记录\n- **新增概念总结**: \n  - ${addedConcepts}\n- **文件与代码变更**: \n  - ${fileChanges}\n\n`;

// 写入当天的日志文件
fs.writeFileSync(logFile, logContent + newEntry, 'utf8');
console.log(`[成功] 已追加今日变更日志: src/doc/extract_log/${dateString}.md`);

// 4. 重建全项目 Hash 快照库
function walk(dir, fileList = []) {
    if (!fs.existsSync(dir)) return fileList;
    const files = fs.readdirSync(dir);
    for (const file of files) {
        const stat = fs.statSync(path.join(dir, file));
        if (stat.isDirectory()) {
            walk(path.join(dir, file), fileList);
        } else if (file.endsWith('.java')) {
            fileList.push(path.join(dir, file));
        }
    }
    return fileList;
}

const srcDir = path.join(__dirname, '../../main/java');
const baseDir = path.join(__dirname, '../../..');

// 防御性判断目录是否存在
if (fs.existsSync(srcDir)) {
    const javaFiles = walk(srcDir);
    const traceData = javaFiles.map(file => {
        const content = fs.readFileSync(file);
        const hash = crypto.createHash('md5').update(content).digest('hex');
        // 转化为跨平台的 POSIX 标准相对路径
        const relativePath = path.relative(baseDir, file).replace(/\\/g, '/');
        return { File: relativePath, Hash: hash };
    });

    // 插入防手动修改警告语
    traceData.unshift({
        "_WARNING_": "================ 请勿手动修改此文件！它由系统脚本全自动生成与维护。手动修改将导致增量分析报错。 ================",
        "File": "IGNORE",
        "Hash": "IGNORE"
    });

    // 覆盖更新 JSON 快照底库
    const stateFile = path.join(__dirname, 'extk_trace_state.json');
    fs.writeFileSync(stateFile, JSON.stringify(traceData, null, 4), 'utf8');
    console.log(`[成功] 项目共 ${traceData.length - 1} 个 Java 文件的防篡改哈希快照已刷新至 extk_trace_state.json`);
} else {
    console.error(`[警告] 找不到 src/main/java 目录，无法更新防篡改哈希库。`);
}
