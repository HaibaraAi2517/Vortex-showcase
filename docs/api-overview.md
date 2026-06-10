# API Overview

The public showcase only documents API shape. The full implementation remains private.

## Memory

### Store

```http
POST /api/v1/memory/store
Content-Type: application/json

{
  "content": "Deployment preference: use dry-run validation before production rollout.",
  "namespace": "agent-session-1",
  "tags": ["deployment", "preference"],
  "reasoningChainId": "chain-42",
  "pinTtlMillis": 60000
}
```

### Recall

```http
POST /api/v1/memory/recall
Content-Type: application/json

{
  "query": "What deployment validation should be used?",
  "namespace": "agent-session-1",
  "topK": 5,
  "tokenBudget": 1024,
  "tags": ["deployment"],
  "scenario": "coding"
}
```

### Feedback

```http
POST /api/v1/memory/feedback
Content-Type: application/json

{
  "recallSessionId": "<recall-session-id>",
  "usedFragmentIds": ["<fragment-id>"],
  "answerAccepted": true
}
```

## Task State

### Create Task

```http
POST /api/v1/tasks
Content-Type: application/json

{
  "description": "Investigate release blocker",
  "namespace": "agent-session-1"
}
```

### Append Node

```http
POST /api/v1/tasks/{taskId}/nodes
Content-Type: application/json

{
  "type": "THOUGHT",
  "content": "Need to inspect current blocker state before proposing next step."
}
```

### Checkpoint and Recover

```http
POST /api/v1/tasks/{taskId}/checkpoint
```

```http
POST /api/v1/tasks/{taskId}/recover
Content-Type: application/json

{
  "checkpointId": "<checkpoint-id>"
}
```

## Observability

```text
GET /api/v1/memory/health
GET /api/v1/memory/health/catalog
GET /api/v1/memory/slo
GET /api/v1/memory/slo/report
```

