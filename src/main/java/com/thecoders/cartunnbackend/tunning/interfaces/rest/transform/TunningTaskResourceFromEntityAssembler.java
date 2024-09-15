package com.thecoders.cartunnbackend.tunning.interfaces.rest.transform;

import com.thecoders.cartunnbackend.tunning.domain.model.aggregates.TunningTask;
import com.thecoders.cartunnbackend.tunning.interfaces.rest.resources.TunningTaskResource;

public class TunningTaskResourceFromEntityAssembler {
    public static TunningTaskResource toResourceFromEntity(TunningTask entity) {
        return new TunningTaskResource(entity.getId(), entity.getModifiedPart(), entity.getDate(), entity.getStatus());
    }
}

