package com.doomonafireball.betterpickers.timepicker;

import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.doomonafireball.betterpickers.R;

import java.util.Vector;

/**
 * Dialog to set alarm time.
 */
public class TimePickerDialogFragment extends DialogFragment {

	private static final String THEME_RES_ID_KEY = "TimePickerDialogFragment_ThemeResIdKey";

	private Button mSet, mCancel;
	private TimePicker mPicker;

	private int mTheme = -1;
	private View mDividerOne, mDividerTwo;
	private int mDividerColor;
	private ColorStateList mTextColor;
	private int mButtonBackgroundResId;
	private int mDialogBackgroundResId;

	public static TimePickerDialogFragment newInstance(int themeResId) {
		final TimePickerDialogFragment frag = new TimePickerDialogFragment();
		Bundle args = new Bundle();
		args.putInt(THEME_RES_ID_KEY, themeResId);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();
		if (args != null && args.containsKey(THEME_RES_ID_KEY)) {
			mTheme = args.getInt(THEME_RES_ID_KEY);
		}

		setStyle(DialogFragment.STYLE_NO_TITLE, 0);

		// Init defaults
		mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
		mButtonBackgroundResId = R.drawable.button_background_dark;
		mDividerColor = getResources().getColor(R.color.default_divider_color_dark);
		mDialogBackgroundResId = R.drawable.dialog_full_holo_dark;

		if (mTheme != -1) {

			TypedArray a = getActivity().getApplicationContext().obtainStyledAttributes(mTheme, R.styleable.BetterPickersDialogFragment);

			mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
			mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground, mButtonBackgroundResId);
			mDividerColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpDividerColor, mDividerColor);
			mDialogBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDialogBackground, mDialogBackgroundResId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.time_picker_dialog, null);
		mSet = (Button) v.findViewById(R.id.set_button);
		mCancel = (Button) v.findViewById(R.id.cancel_button);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				notifyListenersOnCancel();
				dismiss();
			}
		});
		mPicker = (TimePicker) v.findViewById(R.id.time_picker);
		mPicker.setSetButton(mSet);
		mSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				notifyListenersOnTimeSet();
				dismiss();
			}
		});

		mDividerOne = v.findViewById(R.id.divider_1);
		mDividerTwo = v.findViewById(R.id.divider_2);
		mDividerOne.setBackgroundColor(mDividerColor);
		mDividerTwo.setBackgroundColor(mDividerColor);
		mSet.setTextColor(mTextColor);
		mSet.setBackgroundResource(mButtonBackgroundResId);
		mCancel.setTextColor(mTextColor);
		mCancel.setBackgroundResource(mButtonBackgroundResId);
		mPicker.setTheme(mTheme);
		getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

		return v;
	}

	public interface TimePickerDialogHandler {

		void onDialogTimeSet(int hourOfDay, int minute);

		void onDialogCancel();
	}



	/**
	 * Allows to register a listener object to be notified when relevant events like {@link TimePickerDialogHandler#onDialogTimeSet(int, int)} happen. 
	 * 
	 * @param listener
	 * 					- listener to be notified of TimeSetListener's events
	 */

	private Vector<TimePickerDialogHandler> _timePickerDialogListeners = new Vector<TimePickerDialogHandler>();

	void setTimeSetListeners(Vector<TimePickerDialogHandler> listeners) {
		_timePickerDialogListeners = listeners;
	}


	/**
	 * Iterates over all the TimePickerDialogHandler registered listeners and dispatches the onDialogTimeSet() with the preselected time
	 * 
	 */
	private void notifyListenersOnTimeSet() {
		for (TimePickerDialogHandler listener : _timePickerDialogListeners) {
			listener.onDialogTimeSet(mPicker.getHours(), mPicker.getMinutes());
		}
	}


	/**
	 * Iterates over all the TimePickerDialogHandler registered listeners and dispatches the onDialogTimeSet() with the preselected time
	 * 
	 */
	private void notifyListenersOnCancel() {
		for (TimePickerDialogHandler listener : _timePickerDialogListeners) {
			listener.onDialogCancel();
		}
	}

}