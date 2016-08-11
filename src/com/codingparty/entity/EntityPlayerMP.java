package com.codingparty.entity;

import com.codingparty.file.setting.ControlSettings;
import com.codingparty.math.MathHelper;
import com.codingparty.model.Models;

import math.Vector3f;

public class EntityPlayerMP extends EntityMP {
	
	protected static final float MAX_SPEED = 10f;
	protected static final float TURN_SPEED = 60f;
	
	float yRotation = 0;
	
	public EntityPlayerMP() {
		this(new Vector3f(0, 1, 0));
	}
	
	public EntityPlayerMP(Vector3f position) {
		super(position, Models.playerModel);
	}
	
	@Override
	public void updatePosition(float newPosX, float newPosY, float newPosZ, double deltaTime) {
	}

	@Override
	public void updateRotation(float newRotX, float newRotY, float newRotZ, double deltaTime) {
		
	}

	public void moveForward() {
		acceleration.x += (float)(MathHelper.cos(rotation.y));
		acceleration.z -= (float)(MathHelper.sin(rotation.y));
	}
	
	public void moveBackward() {
		acceleration.x += (float)-(MathHelper.cos(rotation.y));
		acceleration.z -= (float)-(MathHelper.sin(rotation.y));
	}
	
	public void moveLeft() {
		yRotation += (float)Math.toRadians(TURN_SPEED);
	}
	
	public void moveRight() {
		yRotation -= (float)Math.toRadians(TURN_SPEED);
	}
	
	@Override
	public void update(double deltaTime) {
		
		rotationAcceleration.set(0, yRotation, 0);
		Vector3f.add(rotationVelocity, (Vector3f)rotationAcceleration, rotationVelocity);
		yRotation = 0;
		float rotVelLength = rotationVelocity.lengthSquared();
		if (rotVelLength != 0) {
			if (rotationAcceleration.lengthSquared() == 0) {
				friction.set(rotationVelocity).negate().scale(0.8f);
			}
			else {
				friction.set(rotationVelocity).negate().scale((1f / ControlSettings.cameraRotationSensitivity.DEFAULT_VALUE));
			}
			
			Vector3f.add(rotationVelocity, friction, rotationVelocity);
			if (rotationVelocity.lengthSquared() < MIN_VELOCITY) rotationVelocity.set(0, 0, 0);
		}
		
		rotation.y += rotationVelocity.y;
		
		////////////////////////////////////////
		
		double maxSpeed = MAX_SPEED * deltaTime;
		if (velocity.lengthSquared() >= maxSpeed * maxSpeed) {
			acceleration.set(0, 0, 0);
		}
		Vector3f.add(velocity, (Vector3f)(acceleration.scale((float)deltaTime)), velocity);
		
		float velocityLength = velocity.lengthSquared();
		if (velocityLength != 0) {
			friction.set(velocity);
			friction.normalise().negate().scale(0.004f);
			Vector3f.add(velocity, friction, velocity);
			if (velocity.lengthSquared() < MIN_VELOCITY) {
				velocity.set(0, 0, 0);
			}
		}
		Vector3f.add(position, velocity, position);
	}
}
