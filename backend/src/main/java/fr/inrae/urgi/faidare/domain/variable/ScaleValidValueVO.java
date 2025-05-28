package fr.inrae.urgi.faidare.domain.variable;

import java.util.List;

/**
 * @author gcornut
 */
public class ScaleValidValueVO implements BrapiScaleValidValue {
    private Double min;
    private Double max;
    private List<String> categories;

    @Override
    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    @Override
    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
