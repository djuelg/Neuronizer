package de.djuelg.neuronizer.domain.model;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Domi on 26.03.2017.
 *
 * Wrapper class for maybe present deadline
 */
public class Deadline {

    private final Date deadline;

    public Deadline() {
        this(null);
    }

    public Deadline(Date deadline) {
        this.deadline = deadline;
    }

    public Deadline(long deadline) {
        this.deadline = new Date(deadline);
    }

    public Date getDate() {
        return deadline;
    }

    /**
     * It is not possible to return an Optional instead of that we have to
     * check if a deadline is present with this method
     */
    public boolean hasDeadline() {
        return deadline != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deadline deadline1 = (Deadline) o;
        return Objects.equals(deadline, deadline1.deadline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deadline);
    }
}
