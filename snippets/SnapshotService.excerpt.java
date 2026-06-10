// Redacted design excerpt. This is not the full implementation.
public final class SnapshotServiceExcerpt {

    public TaskState appendNode(String taskId, AppendNodeCommand command) {
        validator.validate(command);

        wal.append(taskId, Operation.APPEND_NODE, command);

        var state = taskRegistry.requireActive(taskId);
        state.getDag().append(command.toNode());

        checkpointScheduler.recordMutation(taskId);
        return state;
    }

    public TaskState recover(String taskId, String checkpointId) {
        var base = checkpoints.loadBase(taskId, checkpointId);
        var deltas = checkpoints.loadDeltasAfter(base);
        var walEntries = wal.readAfter(taskId, base.sequenceNumber());

        return recoveryEngine.replay(base, deltas, walEntries);
    }
}

