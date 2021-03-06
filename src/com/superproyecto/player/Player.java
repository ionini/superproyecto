package com.superproyecto.player;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.util.Constants;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseLinear;

import com.superproyecto.game.Control;
import com.superproyecto.game.Game;
import com.superproyecto.objects.Entity;


public class Player extends Entity implements IOnScreenControlListener {

	/*
	 * PRIVATE FIELDS
	 */
	private final float mSpeedModifier = 300.0f;
	private Control mControl;
	private TMXLayer mTMXLayer;
	private Path mPath;
	private int mWaypointIndex;
	private PathModifier mPathModifier;
	
	/*
	 * CONSTRUCTORS
	 */
	public Player(Game pEngine, TMXLayer pTMXLayer) {
		super(pEngine);
		this.mTMXLayer = pTMXLayer;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onControlChange(BaseOnScreenControl pBaseOnScreenControl,
			float pValueX, float pValueY) {
		// TODO Auto-generated method stub

		this.mAnimatedSprite.unregisterEntityModifier(this.mPathModifier);
		
		float moveToXTile = this.mPosition.getX() * 32 * pValueX;
		float moveToYTile = this.mPosition.getY() * 32 * pValueY;

		// If the user is touching the screen Puts the touch events into an array
		final float[] pToTiles = this.mGame.getScene().convertLocalToSceneCoordinates(moveToXTile, moveToYTile);

		// Gets where to go
		TMXTile tmxTilePlayerTo = this.mTMXLayer.getTMXTileAt(pToTiles[Constants.VERTEX_INDEX_X],
				pToTiles[Constants.VERTEX_INDEX_Y]);
		
		// Creates a path to that tile
		this.mPath = new Path(2).to(this.mPosition.getX(), this.mPosition.getY())
								.to(230, 150);
		//.to(tmxTilePlayerTo.getTileX(), tmxTilePlayerTo.getTileY());
		
		this.mPathModifier = new PathModifier(30, this.mPath, null, new IPathModifierListener() {
			@Override
			public void onPathStarted(final PathModifier pPathModifier, final IEntity pEntity) {
				Player.this.isWalking = true;
				Debug.d("onPathStarted");
			}

			@Override
			public void onPathWaypointStarted(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointStarted:  " + pWaypointIndex);
				if(Player.this.isWalking) return;
				switch(pWaypointIndex) {
					case 0:
						Player.this.mAnimatedSprite.animate(new long[]{200, 200, 200}, 6, 8, true);
						break;
					case 1:
						Player.this.mAnimatedSprite.animate(new long[]{200, 200, 200}, 3, 5, true);
						break;
					case 2:
						Player.this.mAnimatedSprite.animate(new long[]{200, 200, 200}, 0, 2, true);
						break;
					case 3:
						Player.this.mAnimatedSprite.animate(new long[]{200, 200, 200}, 9, 11, true);
						break;
				}
			}

			@Override
			public void onPathWaypointFinished(final PathModifier pPathModifier, final IEntity pEntity, final int pWaypointIndex) {
				Debug.d("onPathWaypointFinished: " + pWaypointIndex);
				Player.this.isWalking = false;
			}

			@Override
			public void onPathFinished(final PathModifier pPathModifier, final IEntity pEntity) {
				Player.this.isWalking = false;
				Debug.d("onPathFinished");
			}
		}, EaseLinear.getInstance());

		this.mAnimatedSprite.registerEntityModifier(this.mPathModifier);
		
	}

	public PathModifier getPathModifier() {
		return mPathModifier;
	}

	public void setPathModifier(PathModifier pPathModifier) {
		this.mPathModifier = pPathModifier;
	}
	
}
