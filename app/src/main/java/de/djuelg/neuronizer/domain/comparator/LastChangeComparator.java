package de.djuelg.neuronizer.domain.comparator;

import java.util.Comparator;

/**
 * Created by Domi on 06.08.2017.
 */

public class LastChangeComparator implements Comparator<PreviewCompareable> {

    @Override
    public int compare(PreviewCompareable first, PreviewCompareable scnd) {
        return Long.compare(scnd.getChangedAt().getTime(), first.getChangedAt().getTime());
    }
}
