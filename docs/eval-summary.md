# Evaluation Summary

This document summarizes the accepted real-agent memory workload result without exposing raw private reports.

## v3.1 Real-Agent Workload

| Field | Value |
| --- | --- |
| Workload | v3.1 real-agent memory workload |
| Case count | 20 |
| Audit rounds | 3 |
| Modes | Baseline-NoMemory, Vortex-Memory, Vortex-RecoveredMemory |
| Gate result | Passed |

## Result

| Mode | Correct | Notes |
| --- | --- | --- |
| Baseline-NoMemory | 0/20 | Model receives no relevant long-term memory |
| Vortex-Memory | 20/20 | Model receives Vortex-recalled memory |
| Vortex-RecoveredMemory | 20/20 | L1 is constrained; answer depends on lower-tier recovery |

## Why This Matters

The workload is designed to test behaviors that appear in long-running Agent sessions:

- current state overrides stale state
- multi-fragment synthesis
- namespace and alias conflicts
- checkpoint continuation
- tool policy recall
- branch decision continuation
- avoiding repeated completed work

## What Is Redacted

The raw reports are not included because they contain environment-specific paths and provider metadata. This showcase retains only the result contract and system-level interpretation.

## Interpretation Boundary

This result proves controlled workload memory/recovery behavior. It does not claim production multi-tenant readiness, security hardening, or long-term high-concurrency stability.

