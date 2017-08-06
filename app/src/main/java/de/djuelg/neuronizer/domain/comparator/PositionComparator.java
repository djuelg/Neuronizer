package de.djuelg.neuronizer.domain.comparator;

import java.util.Comparator;

/**
 * Created by Domi on 06.08.2017.
 */

public class PositionComparator implements Comparator<PositionCompareable> {

    @Override
    public int compare(PositionCompareable first, PositionCompareable scnd) {
        return Integer.compare(scnd.getPosition(), first.getPosition());
    }
}
