package com.supermap.sample.positiontraffic;

import com.supermap.services.components.commontypes.Feature;

public interface MileagePositionProvider {
    public Feature getFeature(String json);
}
