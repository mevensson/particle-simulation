package com.okayboom.particlesim.collision;

import java.util.List;

import com.okayboom.particlesim.physics.Box;

public interface SpatialMap<V> {

	void add(Box box, V value);

	List<V> get(Box box);

}
