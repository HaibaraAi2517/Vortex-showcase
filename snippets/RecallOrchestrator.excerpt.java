// Redacted design excerpt. This is not the full implementation.
public final class RecallOrchestratorExcerpt {

    public RecallResult recall(RecallQuery query) {
        var sessionId = newRecallSessionId();
        var queryVector = embedding.embed(query.query());

        var l1Candidates = l1.getAll(query.namespace()).stream()
                .filter(fragment -> tagFilter.accepts(fragment, query.tags()))
                .map(fragment -> score(fragment, queryVector, "L1"))
                .sorted(byScoreDescending())
                .toList();

        var selected = tokenBudget.select(l1Candidates, query.topK(), query.tokenBudget());

        if (selected.needsMore()) {
            var l2Candidates = l2.search(query.namespace(), queryVector, query.topK());
            var enriched = enrichment.loadFullFragments(l2Candidates);
            selected = tokenBudget.merge(selected, enriched);
            l1.admitAll(selected.recoveredFragments());
        }

        learner.recordRecallSession(sessionId, query, selected);
        return selected.toRecallResult(sessionId);
    }
}

