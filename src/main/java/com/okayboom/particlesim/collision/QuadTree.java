package com.okayboom.particlesim.collision;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.okayboom.particlesim.physics.Box;

public class QuadTree<V> implements SpatialMap<V> {

	private final static int DEFAULT_LEAF_LIMIT = 8;

	private final List<Pair> pairs = new ArrayList<>();
	private final int leafLimit;
	private List<QuadTree<V>> children = null;

	public QuadTree(int leafLimit) {
		this.leafLimit = leafLimit;
	}

	private Pair pair(V value, Box box) {
		return new Pair(value, box);
	}

	class Pair {
		final V value;
		final Box box;

		Pair(V value, Box box) {
			this.value = value;
			this.box = box;
		}
	}

	public static final <X> QuadTree<X> empty(Box boundary) {
		return new QuadTree<X>(DEFAULT_LEAF_LIMIT);
	}

	public static final <X> QuadTree<X> empty(Box boundary, int leafLimit) {

		return new QuadTree<X>(leafLimit);
	}

	public boolean isLeaf() {
		return children == null;
	}

	@Override
	public void add(Box box, V value) {
		Pair p = pair(value, box);
		boolean hasReachedLimit = pairs.size() >= leafLimit;

		if (isLeaf() && hasReachedLimit)
			transformFromLeafToInnerNode();

		if (isLeaf()) {
			pairs.add(p);
		} else {
			distributeToChildren(p);
		}
	}

	private void distributeToChildren(QuadTree<V>.Pair p) {
		// TODO Auto-generated method stub

	}

	private void transformFromLeafToInnerNode() {
		children = new ArrayList<QuadTree<V>>(4);
		children.add(empty(Box.box(0, 0, 0, 0)));
		children.add(empty(Box.box(0, 0, 0, 0)));
		children.add(empty(Box.box(0, 0, 0, 0)));
		children.add(empty(Box.box(0, 0, 0, 0)));
	}

	@Override
	public List<V> get(Box box) {
		return pairs.stream().filter(p -> p.box.doIntersect(box))
				.map(p -> p.value).collect(Collectors.toList());
	}
}
