package com.supermap.sample.position;

import com.supermap.services.components.commontypes.Feature;

public interface MileagePositionProvider {
    public Feature getFeature(String json);
}
