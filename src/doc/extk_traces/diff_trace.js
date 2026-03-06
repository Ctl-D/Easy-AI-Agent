const fs = require('fs');
const crypto = require('crypto');
const path = require('path');

// 递归遍历文件
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

const baseDir = path.resolve(__dirname, '../../..');
const srcDir = path.join(baseDir, 'src/main/java');
const traceFile = path.join(__dirname, 'extk_trace_state.json');

// 读取老留痕数据
let oldState = new Map();
if (fs.existsSync(traceFile)) {
    try {
        const data = JSON.parse(fs.readFileSync(traceFile, 'utf8'));
        data.forEach(item => oldState.set(item.File, item.Hash));
    } catch (e) {
        console.warn("读取历史留痕失败，将进行全量分析。");
    }
}

// 扫描当前状态
const currentFiles = walk(srcDir);
const changedFiles = [];
const newStateArr = [];

currentFiles.forEach(file => {
    const content = fs.readFileSync(file);
    const currentHash = crypto.createHash('md5').update(content).digest('hex');
    const relativePath = path.relative(baseDir, file).replace(/\\/g, '/');

    newStateArr.push({ File: relativePath, Hash: currentHash });

    const oldHash = oldState.get(relativePath);
    if (!oldHash) {
        changedFiles.push({ file: relativePath, status: 'ADDED' });
    } else if (oldHash !== currentHash) {
        changedFiles.push({ file: relativePath, status: 'MODIFIED' });
    }
});

// 输出供 AI 读取的差异结果
if (changedFiles.length === 0) {
    console.log("NO_CHANGES_DETECTED");
} else {
    console.log("CHANGES_DETECTED:");
    changedFiles.forEach(change => {
        console.log(`[${change.status}] ${change.file}`);
    });
}
