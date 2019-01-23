package fr.inra.urgi.gpds.domain.brapi.v1.response;

import java.util.List;

/**
 * @author gcornut
 */
class BrapiDataImpl<T> implements BrapiData<T> {

    private final List<T> data;

    BrapiDataImpl(List<T> data) {
        this.data = data;
    }

    @Override
    public List<T> getData() {
        return data;
    }

}
