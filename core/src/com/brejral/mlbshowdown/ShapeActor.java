package com.brejral.mlbshowdown;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ShapeActor extends Actor {
	public ShapeRenderer renderer = new ShapeRenderer();
	public String shape;
	public ShapeType type;
	public float[] verts;
	public float width;
	public float height;
	public float radius;
	
	/** 
	 * Creates a Triangle ShapeActor
	 * @param st - ShapeType (must be Filled or Line)
	 * @param vertices - vertices for triangle [x1, y1, x2, y2, x3, y3]
	 */
	public ShapeActor(ShapeType st, float[] vertices) {
		shape = "Triangle";
		type = st;
		verts = vertices;
	}
	
	/**
	 * Creates a Rectangle ShapeActor
	 * @param st - ShapeType (must be Filled or Line)
	 * @param vertices - Lower left vertex of Rectangle [x1, y1]
	 * @param w - width of Rectangle
	 * @param h - height of Rectangle
	 */
	public ShapeActor(ShapeType st, float[] vertices, float w, float h) {
		shape = "Rectangle";
		type = st;
		verts = vertices;
		width = w;
		height = h;
	}
	
	/**
	 * Creates a Circle ShapeActor
	 * @param st - ShapeType
	 * @param vertices - Center vertex of Circle [x1, y1]
	 * @param r - radius of Circle
	 */
	public ShapeActor(ShapeType st, float[] vertices, float r) {
		shape = "Circle";
		type = st;
		verts = vertices;
		radius = r;
	}
	
	/**
	 * Sets the vertices of the shape
	 * @param vertices - float array of vertices
	 */
	public void setVertices(float[] vertices) {
		verts = vertices;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		renderer.begin(type);
		renderer.setColor(getColor());
		switch(shape) {
		case "Rectangle":
			renderer.rect(verts[0], verts[1], width, height);
			break;
		case "Triangle":
			renderer.triangle(verts[0], verts[1], verts[2], verts[3], verts[4], verts[5]);
			break;
		case "Circle":
			renderer.circle(verts[0], verts[1], radius);
			break;
		}
		renderer.end();
		batch.begin();
	}
}
