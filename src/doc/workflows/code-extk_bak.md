---
description: 提取基于 langchain4j 的 AI 项目中的核心概念知识点，并总结成通俗易懂的文档（忽略测试代码）
---

# 任务目标

分析 `src/main/java` 下新增或有改动的方法，提取相关 AI/框架概念，用大白话解释并提供 langchain4j 的代码示例，增量补充到 `src/doc/learning_record.md`。如方法缺少 JavaDoc，需主动补充。

**忽略范围**：`src/test` 目录、常量类（Constants）、枚举类（Enums）。
**禁止**：执行任何 `git` 命令。

# 执行步骤

## 1. 检测变更文件

执行：`node src/doc/extk_traces/diff_trace.js`

- 输出 `NO_CHANGES_DETECTED` → 直接结束，不做任何记录。
- 输出变更文件列表 → 读取变更文件，继续下面步骤。

## 2. 确认知识点是否已记录（用 INDEX.md，禁止读 learning_record.md 全文）

读取 `src/doc/INDEX.md`（轻量索引表），根据其中已有的**关键词/类名标签**列，判断本次变更涉及的核心概念是否已经被总结过：
- 已有 → 仅做拓展补充，不重复建章节。
- 未有 → 提取新概念，建立新章节追加到 `learning_record.md`。

## 3. 自动补充 JavaDoc

检查变更方法上方是否有 JavaDoc：
- 无注释 → 补充生成，说明方法**作用**及**涉及的 AI/langchain4j 知识点**。
- 有注释但不准确 → 修正；准确则不动。

## 4. 提取概念 & 撰写知识点

对新概念依次输出：
1. **概念定义**：正式定义 + 该概念在 langchain4j 框架中的实现思路（必须包含）。
2. **白话文解释**：生活比喻，面向非技术人员。
3. **框架使用示例**（固定子标题，不得变更）：展示框架最高级/最新用法，有多种实现方式时只保留最推荐的一种。示例必须覆盖该概念在框架中支持的所有相关类型（不仅限于代码中出现的）。加注释说明入参/出参/逻辑。

如代码注解中含"**注意**"等警示说明，必须深入剖析其限制/风险，纳入对应知识点。

## 5. 增量写入 learning_record.md

追加到 `src/doc/learning_record.md` 末尾。
**格式要求**：追加前检查上一个代码块（` ``` `）已正确闭合，新章节标题不得写在代码块内部。

## 6. 自动化收尾（记录日志 + 刷新哈希 + 更新索引）

所有写入完毕后，执行：

```
node src/doc/extk_traces/finish_extk.js "<概念总结简述>" "<文件变更简述>" "<章节号>" "<章节名称>" "<关键词1,关键词2,...>"
```

- **arg[2] 章节号**：新建章节时填写（如 `"11"`）；仅补充已有章节时传 `""`。
- **arg[3] 章节名称**：对应 INDEX.md 第2列（如 `"工具调用 (Tool Calling)"`）。
- **arg[4] 关键词列表**：逗号分隔，**不含反引号**，脚本自动添加（如 `"@Tool,ToolExecutionRequest"`）。

示例（新建章节）：
```
node src/doc/extk_traces/finish_extk.js "梳理了工具调用概念" "为ToolAgent.java补充JavaDoc" "11" "工具调用 (Tool Calling)" "@Tool,ToolExecutionRequest,ToolExecutionResult"
```

示例（仅补充已有章节）：
```
node src/doc/extk_traces/finish_extk.js "补充了RAG进阶内容" "更新EasyRAGAgent.java注释" "" "" ""
```

该命令将自动完成：追加每日日志 + 更新 INDEX.md 索引（有新章节时）+ 刷新全项目哈希快照。