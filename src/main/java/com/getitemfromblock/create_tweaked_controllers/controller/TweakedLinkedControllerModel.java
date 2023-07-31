package com.getitemfromblock.create_extended_controllers.controller.extended;

import com.simibubi.create.foundation.item.render.CreateCustomRenderedItemModel;

import net.minecraft.client.resources.model.BakedModel;

public class ExtendedLinkedControllerModel extends CreateCustomRenderedItemModel
{

	public ExtendedLinkedControllerModel(BakedModel template) {
		super(template, "extended_linked_controller");
		addPartials("powered", "button");
	}

}
