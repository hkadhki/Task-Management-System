package com.example.taskmanagersystem.repository;

import com.example.taskmanagersystem.dto.FindTasksDto;
import com.example.taskmanagersystem.model.TaskEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/// A utility class for building {@link Specification} objects for querying {@link TaskEntity}.
@Component
public class TaskSpecification {

    /**
     * Builds a {@link Specification} for {@link TaskEntity} based on the provided filter parameters.
     * <p>
     * Each filter will apply a condition only if the corresponding field in {@link FindTasksDto} is not null.
     * </p>
     *
     * @param params the filter parameters for searching tasks.
     * @return the specification object to use in a query.
     */
    public Specification<TaskEntity> build(FindTasksDto params) {
        return withAuthor(params.getAuthor())
                .and(withExecutor(params.getExecutor()))
                .and(withNonPriority(params.getNonPriority()))
                .and(withPriority(params.getPriority()))
                .and(withStatus(params.getStatus()))
                .and(withNonStatus(params.getNonStatus()))
                .and(withCountCommentsLess(params.getCountCommentsLess()))
                .and(withCountCommentsGreater(params.getCountCommentsGreater()))
                .and(withCountCommentsEqual(params.getCountCommentsEqual()))
                ;
    }

    /**
     * Creates a {@link Specification} that filters tasks by the author.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param author the username of the task's author.
     * @return the specification to filter by author.
     */
    private Specification<TaskEntity> withAuthor(String author) {
        return (root, query, cb) -> author == null ? cb.conjunction() : cb.equal(root.get("author").get("username"), author);
    }

    /**
     * Creates a {@link Specification} that filters tasks by the executor.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param executor the username of the task's executor.
     * @return the specification to filter by executor.
     */
    private Specification<TaskEntity> withExecutor(String executor) {
        return (root, query, cb) -> executor == null ? cb.conjunction() : cb.equal(root.get("executor").get("username"), executor);
    }

    /**
     * Creates a {@link Specification} that filters tasks by status.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param status the status of the task.
     * @return the specification to filter by status.
     */
    private Specification<TaskEntity> withStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    /**
     * Creates a {@link Specification} that filters tasks by excluding a specific status.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param status the status to exclude.
     * @return the specification to exclude tasks with the given status.
     */
    private Specification<TaskEntity> withNonStatus(String status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.notEqual(root.get("status"), status);
    }

    /**
     * Creates a {@link Specification} that filters tasks by priority.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param priority the priority of the task.
     * @return the specification to filter by priority.
     */
    private Specification<TaskEntity> withPriority(String priority) {
        return (root, query, cb) -> priority == null ? cb.conjunction() : cb.equal(root.get("priority"), priority);
    }

    /**
     * Creates a {@link Specification} that filters tasks by excluding a specific priority.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param priority the priority to exclude.
     * @return the specification to exclude tasks with the given priority.
     */
    private Specification<TaskEntity> withNonPriority(String priority) {
        return (root, query, cb) -> priority == null ? cb.conjunction() : cb.notEqual(root.get("priority"), priority);
    }

    /**
     * Creates a {@link Specification} that filters tasks by the number of comments being less than a specified value.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param countCommentsLess the upper bound for the number of comments.
     * @return the specification to filter tasks by the number of comments less than the specified value.
     */
    private Specification<TaskEntity> withCountCommentsLess(Integer countCommentsLess) {
        return (root, query, cb) -> countCommentsLess == null ? cb.conjunction() : cb.le(cb.size(root.get("comments")), countCommentsLess);
    }

    /**
     * Creates a {@link Specification} that filters tasks by the number of comments being greater than a specified value.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param countCommentsGreater the lower bound for the number of comments.
     * @return the specification to filter tasks by the number of comments greater than the specified value.
     */
    private Specification<TaskEntity> withCountCommentsGreater(Integer countCommentsGreater) {
        return (root, query, cb) -> countCommentsGreater == null ? cb.conjunction() : cb.gt(cb.size(root.get("comments")), countCommentsGreater);
    }

    /**
     * Creates a {@link Specification} that filters tasks by the number of comments being equal to a specified value.
     * <p>
     * If the count is null, no filtering will occur for this field.
     * </p>
     *
     * @param countCommentsEqual the exact number of comments to match.
     * @return the specification to filter tasks by the number of comments equal to the specified value.
     */
    private Specification<TaskEntity> withCountCommentsEqual(Integer countCommentsEqual) {
        return (root, query, cb) -> countCommentsEqual == null ? cb.conjunction() : cb.equal(cb.size(root.get("comments")), countCommentsEqual);
    }


}
