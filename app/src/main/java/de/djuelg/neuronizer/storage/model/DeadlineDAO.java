package de.djuelg.neuronizer.storage.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Domi on 26.03.2017.
 *
 * Wrapper class for maybe present deadline
 */
public class DeadlineDAO extends RealmObject {

    private Date deadline;

    public DeadlineDAO() {
        this(null);
    }

    public DeadlineDAO(Date deadline) {
        this.deadline = deadline;
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
}
