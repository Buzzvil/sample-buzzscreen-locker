package com.buzzvil.buzzscreen.sample.locker.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.buzzvil.buzzscreen.sample.locker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LockTutorialFragment.java
 *
 * @author Cos
 */
public class LockTutorialFragment extends Fragment {
	List<Integer> guideResources = new ArrayList<>(Arrays.asList(R.drawable.lockscreen_tutorial_2, R.drawable.lockscreen_tutorial_3));
	public interface OnTutorialListner {
		void onFinish();
	}

	final int animationDuration = 200;
	private Animation fadeOut, fadeIn;

	private ImageView imageGuide;
	private TextView textNext;

	private OnTutorialListner onTutorialListner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_lock_tutorial, container, false);

		imageGuide = (ImageView) view.findViewById(R.id.imageGuide);
		textNext = (TextView) view.findViewById(R.id.textNext);
		view.findViewById(R.id.vGroupParent).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		textNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation animation = imageGuide.getAnimation();
				if (animation == null || animation.hasEnded()) {
					showNextGuide();
				}
			}
		});

		initAnimations();

		return view;
	}

	private void initAnimations() {
		fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setDuration(animationDuration);

		fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		fadeIn.setDuration(animationDuration);

		fadeOut.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				imageGuide.setImageResource(guideResources.remove(0));
				imageGuide.startAnimation(fadeIn);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	public void setOnTutorialListener(OnTutorialListner onTutorialListner) {
		this.onTutorialListner = onTutorialListner;
	}

	void showNextGuide() {
		if (guideResources.isEmpty()) {
			if (this.onTutorialListner != null) {
				this.onTutorialListner.onFinish();
			}

			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		} else {
			imageGuide.startAnimation(fadeOut);
		}
	}
}
