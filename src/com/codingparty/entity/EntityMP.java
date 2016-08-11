package com.codingparty.entity;

import com.codingparty.entity.util.Color;
import com.codingparty.model.util.ModelRaw;

import math.Vector3f;

public abstract class EntityMP extends Entity {

	public static final float ILLEGAL_DEVIATION = 5f;
	public static final float MAX_DEVIATION = 1f;
	
	public EntityMP(Vector3f position) {
		this(position, null);
	}
	
	public EntityMP(ModelRaw rawModel) {
		this(new Vector3f(), rawModel);
	}
	
	public EntityMP(Vector3f pos, ModelRaw rawModel) {
		this(pos, new Vector3f(), rawModel, new Vector3f(1, 1, 1));
	}
	
	public EntityMP(Vector3f pos, Vector3f rot, ModelRaw rawModel, Vector3f entityScale) {
		super(pos, new Vector3f(), rawModel, entityScale, new Color(1, 1, 1, 1));
	}
	
	public abstract void updatePosition(float newPosX, float newPosY, float newPosZ, double deltaTime);
	public abstract void updateRotation(float newRotX, float newRotY, float newRotZ, double deltaTime);
}
