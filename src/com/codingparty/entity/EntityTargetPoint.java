package com.codingparty.entity;

import com.codingparty.camera.TargetedCamera;
import com.codingparty.file.setting.ControlSettings;
import com.codingparty.math.MathHelper;

import math.Vector3f;

public class EntityTargetPoint extends Entity {

	protected static final float MAX_SPEED = 10f;
	private TargetedCamera camera;
	
	public EntityTargetPoint(Vector3f position) {
		super(position);
		camera = new TargetedCamera(this);
	}
	
	@Override
	public void update(double deltaTime) {
		
		if (ControlSettings.moveForwardKey.isPressed()) {
			acceleration.x += (float)(MathHelper.sin(camera.getYaw()));
			acceleration.z -= (float)(MathHelper.cos(camera.getYaw()));
		}
		
		if (ControlSettings.moveBackwardKey.isPressed()) {
			acceleration.x += (float)-(MathHelper.sin(camera.getYaw()));
			acceleration.z -= (float)-(MathHelper.cos(camera.getYaw()));
		}
		
		if (ControlSettings.moveLeftKey.isPressed()) {
			acceleration.x -= (float)(MathHelper.sin(camera.getYaw() + 90));
			acceleration.z += (float)(MathHelper.cos(camera.getYaw() + 90));
		}
		
		if (ControlSettings.moveRightKey.isPressed()) {
			acceleration.x += (float)(MathHelper.sin(camera.getYaw() + 90));
			acceleration.z -= (float)(MathHelper.cos(camera.getYaw() + 90));
		}
		 
		 if (ControlSettings.moveUpKey.isPressed()) {
			 acceleration.y += 1f;
		 }
		
		//TODO: SEE IF THERE IS A BETTER WAY TO DO THIS.
		if (ControlSettings.moveDownKey.isPressed()) {
			 acceleration.y -= 1f;
		}
		
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
		camera.update(deltaTime);
	}
	
	public TargetedCamera getCamera() {
		return camera;
	}
}
