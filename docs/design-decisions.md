# Design Decisions

## Why Three Memory Tiers

Long-running Agent sessions need different access patterns:

- hot working context must be fast
- semantically relevant older facts must remain searchable
- durable artifacts must survive restarts and evictions

Vortex separates these concerns into L1, L2, and L3 rather than treating memory as a single cache.

## Why Semantic Eviction

Simple LRU can evict facts that are old but still important to a task. Vortex uses a semantic-LRU style score:

```text
score = alpha * recency + beta * similarity + gamma * importance
```

The private implementation also considers reasoning-chain grouping, pinning, redundancy, novelty, namespace pressure, and regret tracking.

## Why Feedback-Driven Ranking

Static retrieval weights are brittle across scenarios. A coding assistant, chat assistant, and search assistant may prefer different ranking profiles. Feedback closes the loop:

```text
recall -> answer -> feedback -> active/shadow/baseline comparison -> profile update
```

## Why WAL Before State Mutation

Task state must survive process failure. The system follows:

```text
validate-before-WAL
WAL-before-state
```

This makes recovery deterministic and prevents partially applied mutations from becoming invisible to replay.

## Why Redacted Public Showcase

The private repository contains full implementation details, raw evaluation artifacts, local environment metadata, and commercial design decisions. This public repository is intentionally limited to architecture, API shape, and selected excerpts.

