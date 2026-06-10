// Redacted design excerpt. This is not the full implementation.
public final class HierarchicalMemoryControllerExcerpt {

    public StoreResult store(StoreCommand command) {
        requireNamespace(command.namespace());

        var fragments = splitter.split(
                command.content(),
                command.namespace(),
                command.tags(),
                command.reasoningChainId());

        for (var fragment : fragments) {
            fragment.setEmbedding(embedding.embed(fragment.getContent()));
            l1.put(fragment);
            persistence.enqueue(fragment);
        }

        return StoreResult.from(fragments);
    }

    public RecallResult recall(RecallQuery query) {
        return recallOrchestrator.recall(query);
    }

    public void feedback(MemoryFeedback feedback) {
        learner.recordFeedback(feedback);
        slo.recordFeedback(feedback);
    }
}

