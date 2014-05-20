package com.eclecticapps.soladvanced;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g3d.environment.*;

public class solAdvanced implements ApplicationListener
{
	/** Based on...* See: http://blog.xoppa.com/basic-3d-using-libgdx-2 * @author Xoppa */
	float dT;

	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	// solModel sol solR, solC,
	public Model mercModel, venModel, earthModel, marsModel, jupModel;
	public Model satModel, uraModel, nepModel, pluModel;
	public ModelInstance mercury, venus, earth, mars, jupiter;    
    public ModelInstance saturn, uranus, neptune, pluto;
  	public float solR, merD, merR, venD, venR, earR, earD, marR, marD, jupR, jupD;
	public Color merC, venC, earC, marC, jupC;
	public float satR, uraR, nepR, pluR;
	public float satD, uraD, nepD, pluD;
	public Color satC, uraC, nepC, pluC;
	public Color dirLights;
	public float camFact;
	public Vector3 touchPoint;

	public class Planet{
		public float x, y, z, w, h, d;
		public int div;
		public Color c;
		public Model m;
		public ModelInstance i;
	}
	public Planet Sol;


	@Override
	public void create() {

		touchPoint = new Vector3();
		Sol = new Planet();
//		Sol.c.set(Color.ORANGE.add(Color.YELLOW));
		Sol.x = 0;
		Sol.y = 0;
		Sol.z = 0;
		Sol.div = 40;
		Sol.c = new Color(Color.ORANGE.add(Color.YELLOW));
		Sol.w = 695.5f;
		Sol.h = Sol.w;
		Sol.d = Sol.w;
		solR = Sol.w;

		dT = 0;
		// Environment for lights
		environment = new Environment();
		// Model batch to draw all stars/planets
		modelBatch = new ModelBatch();
		// Builds each object
		ModelBuilder modelBuilder = new ModelBuilder();

		dirLights = new Color(Color.WHITE);
		camFact = 250f;
		EnviroLights();
		CameraSet();

		// Sun's diameter is 1 391 000 km
		//	solR = 139.1f;
		//	solC = new Color(Color.ORANGE.add(Color.YELLOW));

		// Mercury's radius is 5 000 km
		merR = 5f;
		// Mercury is 58 000 000 km
		merD = solR + 58f;
		merC = new Color(Color.LIGHT_GRAY);
		venR = 12f;
		venD = solR + 108f;
		venC = new Color(Color.MAGENTA);
		earR = 13f;
		earD = solR + 150f;
		earC = new Color(Color.CYAN);
		marR = 7f;
		marD = solR + 228f;
		marC = new Color(Color.RED);
		jupR = 143f;
		jupD = solR + 778f;
		jupC = new Color(Color.DARK_GRAY);
		satR = 120f;
		satD = solR + 1080f;
		satC = new Color(Color.WHITE);
		uraR = 40f;
		uraD = solR + 1500f;
		uraC = new Color(Color.BLUE);
		nepR = 70f;
		nepD = solR + 2280f;
		nepC = new Color(Color.GREEN);
		pluR = 3f;
		pluD = solR + 7780f;
		pluC = new Color(Color.GRAY);

		//*** Keep together in create
//		solModel = modelBuilder.createSphere(solR, solR, solR, 40, 40,
//											 new Material(ColorAttribute.createDiffuse(solC)),
//											 Usage.Position | Usage.Normal);
//		sol = new ModelInstance(solModel);

		Sol.m = modelBuilder.createSphere(Sol.w, Sol.h, Sol.d, Sol.div, Sol.div,
										  new Material(ColorAttribute.createDiffuse(Sol.c)),
										  Usage.Position | Usage.Normal);
		Sol.i = new ModelInstance(Sol.m);
		//	Sol.i.transform.translate(Sol.x, Sol.y, Sol.z);
		//
		mercModel = modelBuilder.createSphere(merR, merR, merR, 40, 40, 
											  new Material(ColorAttribute.createDiffuse(merC)),
											  Usage.Position | Usage.Normal);
		mercury = new ModelInstance(mercModel);
		mercury.transform.translate(merD, 0, merD);
		//
		venModel = modelBuilder.createSphere(venR, venR, venR, 40, 40, 
											 new Material(ColorAttribute.createDiffuse(venC)),
											 Usage.Position | Usage.Normal);
		venus = new ModelInstance(venModel);
		venus.transform.translate(venD, 0, venD);
		//
		earthModel = modelBuilder.createSphere(earR, earR, earR, 40, 40,
											   new Material(ColorAttribute.createDiffuse(earC)), 
											   Usage.Position | Usage.Normal);
		earth = new ModelInstance(earthModel);
		earth.transform.translate(earD, 0, earD);
		//
		marsModel = modelBuilder.createSphere(marR, marR, marR, 40, 40,
											  new Material(ColorAttribute.createDiffuse(marC)), 
											  Usage.Position | Usage.Normal);
		mars = new ModelInstance(marsModel);
		mars.transform.translate(marD, 0, marD);
		//
		jupModel = modelBuilder.createSphere(jupR, jupR, jupR, 40, 40,
											 new Material(ColorAttribute.createDiffuse(jupC)), 
											 Usage.Position | Usage.Normal);
		jupiter = new ModelInstance(jupModel);
		jupiter.transform.translate(jupD, 0, jupD);
		//
		satModel = modelBuilder.createSphere(satR, satR, satR, 40, 40, 
											 new Material(ColorAttribute.createDiffuse(satC)),
											 Usage.Position | Usage.Normal);
		saturn = new ModelInstance(satModel);
		saturn.transform.translate(satD, 0, satD);
		//
		uraModel = modelBuilder.createSphere(uraR, uraR, uraR, 40, 40,
											 new Material(ColorAttribute.createDiffuse(uraC)), 
											 Usage.Position | Usage.Normal);
		uranus = new ModelInstance(uraModel);
		uranus.transform.translate(uraD, 0, uraD);
		//
		nepModel = modelBuilder.createSphere(nepR, nepR, nepR, 40, 40,
											 new Material(ColorAttribute.createDiffuse(nepC)), 
											 Usage.Position | Usage.Normal);
		neptune = new ModelInstance(nepModel);
		neptune.transform.translate(nepD, 0, nepD);
		//
		pluModel = modelBuilder.createSphere(pluR, pluR, pluR, 40, 40,
											 new Material(ColorAttribute.createDiffuse(pluC)), 
											 Usage.Position | Usage.Normal);
		pluto = new ModelInstance(pluModel);
		pluto.transform.translate(pluD, 0, pluD);
		//*** Keep together in create

		// Set control of camera to touch/pan/pinch
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
	}

	private void EnviroLights(){
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.2f, 0.2f, 1f));
		environment.clear();
		environment.add(new DirectionalLight().set(dirLights, -1f, 0, 0));
		environment.add(new DirectionalLight().set(dirLights, 1f, 0, 0));
		environment.add(new DirectionalLight().set(dirLights , 0, -1f, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, 1f, 0));
		environment.add(new DirectionalLight().set(dirLights, 0, 0, 1f));
		environment.add(new DirectionalLight().set(dirLights, 0, 0, -1f));
	}

	private void CameraSet(){
		cam = new PerspectiveCamera(50, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(jupD + 500f, jupD + 500f, jupD + 500f);
		cam.lookAt(0,0,0);
		cam.near = 10f;
		cam.far = 15000f;
		cam.update();
	}

	@Override
	public void render() {
		dT = Gdx.graphics.getDeltaTime();
		camController.pinchZoomFactor = camFact;
		camController.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		DrawSol();
		TimeElapse();
		
		//   for (DirectionalLight light: environment.directionalLights){
		//			light.set(0.8f, 0.8f, 0.8f, (float)Math.sin(dT), (float)Math.cos(dT), -0.2f);
		//		}
	}

	public void TimeElapse()
	{
		cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		if (Gdx.input.justTouched()) {
			if (pointInPlanet(Sol, touchPoint.x, touchPoint.y)) {
				dirLights = Color.GREEN;
				EnviroLights();
			}
			environment.add(new DirectionalLight().set(Color.RED, touchPoint.x, touchPoint.y, -1f));
		//	dirLights = Color.WHITE;
		//	EnviroLights();
		}
	//	dirLights = Color.WHITE;
	//	EnviroLights();
		//	earth.transform.rotate(1f, 1f, 0, dT);
		//	sol.transform.translate(0, 0, dT);
	}

	public static boolean pointInPlanet (Planet r, float x, float y) {
		return r.x <= x && r.x + r.w >= x && r.y <= y && r.y + r.h >= y;
	}
	public void DrawSol(){
		modelBatch.begin(cam);
		modelBatch.render(Sol.i, environment);
		//	modelBatch.render(sol, environment);
		modelBatch.render(mercury, environment);
		modelBatch.render(venus, environment);
		modelBatch.render(earth, environment);
		modelBatch.render(mars, environment);
		modelBatch.render(jupiter, environment);
		modelBatch.render(saturn, environment);
		modelBatch.render(neptune, environment);
		modelBatch.render(uranus, environment);
		modelBatch.render(pluto, environment);
		modelBatch.end();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		Sol.m.dispose();
//		solModel.dispose();
		earthModel.dispose();
		mercModel.dispose();
		venModel.dispose();
		marsModel.dispose();
		jupModel.dispose();
		satModel.dispose();
		uraModel.dispose();
		nepModel.dispose();
		pluModel.dispose();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
