package com.getitemfromblock.create_tweaked_controllers.controller;

import com.simibubi.create.foundation.item.render.CreateCustomRenderedItemModel;

import net.minecraft.client.resources.model.BakedModel;

public class TweakedLinkedControllerModel extends CreateCustomRenderedItemModel
{

	public TweakedLinkedControllerModel(BakedModel template) {
		super(template, "tweaked_linked_controller");
		addPartials("powered", "button");
	}

}
