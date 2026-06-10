# Architecture

Vortex is organized around two kernel responsibilities:

1. Agent memory: long-context facts, preferences, observations, tool results, and recovery.
2. Agent state: task DAG, checkpoint, WAL, branch, merge, and restart recovery.

## Module View

```text
vortex-app      REST API, health endpoints, eval CLI, integration boundaries
vortex-kernel   memory controller, recall, eviction, learning, snapshot, paging
vortex-storage  L1/L2/L3 storage adapters
vortex-common   shared model, DTO, serialization, health contracts
```

## Memory Write Path

```mermaid
sequenceDiagram
    participant Agent
    participant API
    participant HMC
    participant L1
    participant L2
    participant L3

    Agent->>API: POST /memory/store
    API->>HMC: store(content, namespace, tags)
    HMC->>HMC: split + embed
    HMC->>L1: admit hot fragments
    HMC-->>L2: async vector persistence
    HMC-->>L3: async durable archive
    HMC-->>API: fragmentIds
```

## Recall Path

```mermaid
sequenceDiagram
    participant Agent
    participant API
    participant Recall
    participant L1
    participant L2
    participant L3
    participant Learn

    Agent->>API: POST /memory/recall
    API->>Recall: query(namespace, topK, tokenBudget)
    Recall->>L1: semantic rank
    alt enough candidates
        Recall-->>API: RecallResult
    else L1 insufficient
        Recall->>L2: vector search
        Recall->>L3: enrich missing fragments
        Recall->>L1: re-admit recovered fragments
        Recall-->>API: RecallResult
    end
    Recall->>Learn: record recall session
```

## State Recovery Path

```text
validate input
append WAL
mutate in-memory task state
create checkpoint when needed
recover as FULL checkpoint -> DELTA chain -> WAL replay
```

## Observability

The health layer exposes typed diagnostic signals instead of only raw exceptions. A health report can distinguish:

- persistence failure
- L2 recall miss
- checkpoint recovery failure
- namespace isolation issue
- eviction regret
- learning signal insufficiency

