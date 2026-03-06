const fs = require('fs');
const crypto = require('crypto');
const path = require('path');

// 递归遍历目录找出所有 java 文件
function walk(dir, filelist = []) {
    if (!fs.existsSync(dir)) return filelist;
    fs.readdirSync(dir).forEach(file => {
        const filepath = path.join(dir, file);
        if (fs.statSync(filepath).isDirectory()) {
            walk(filepath, filelist);
        } else if (filepath.endsWith('.java')) {
            filelist.push(filepath);
        }
    });
    return filelist;
}

// 定义目录
const baseDir = path.resolve(__dirname, '../../..'); // 回退到项目根目录
const srcDir = path.join(baseDir, 'src/main/java');
const traceFile = path.join(__dirname, 'extk_trace_state.json');

console.log('开始扫描 Java 文件目录:', srcDir);

const files = walk(srcDir);
const result = files.map(file => {
    // 读取文件内容并计算哈希
    const content = fs.readFileSync(file);
    const hash = crypto.createHash('md5').update(content).digest('hex');
    // 转换为严格的 POSIX 相对路径，便于跨平台比对
    const relativePath = path.relative(baseDir, file).replace(/\\/g, '/');
    return { File: relativePath, Hash: hash };
});

// 插入防手动修改警告语
result.unshift({
    "_WARNING_": "================ 请勿手动修改此文件！它由系统脚本全自动生成与维护。手动修改将导致增量分析报错。 ================",
    "File": "IGNORE",
    "Hash": "IGNORE"
});

// 输出 JSON，保持格式美观
fs.writeFileSync(traceFile, JSON.stringify(result, null, 4), 'utf8');

console.log(`扫描完成！共处理了 ${result.length} 个文件，并成功写入 ${traceFile}`);
