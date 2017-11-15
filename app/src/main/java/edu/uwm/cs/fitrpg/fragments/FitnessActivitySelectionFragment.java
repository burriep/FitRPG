package edu.uwm.cs.fitrpg.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uwm.cs.fitrpg.BuildConfig;
import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.Utils;
import edu.uwm.cs.fitrpg.model.PhysicalActivityType;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FitnessActivitySelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FitnessActivitySelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessActivitySelectionFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6923;
    private Button activityStartButton;
    private RadioGroup activityType;
    private Map<String, PhysicalActivityType> activityTypes;

    public FitnessActivitySelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment FitnessActivitySelectionFragment.
     */
    public static FitnessActivitySelectionFragment newInstance() {
        return new FitnessActivitySelectionFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fitness_activity_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activityStartButton = view.findViewById(R.id.btn_activity_start);
        activityStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTrackingActivity(view);
            }
        });
        activityType = view.findViewById(R.id.activity_type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<PhysicalActivityType> types = PhysicalActivityType.getAll(new DatabaseHelper(getContext()).getReadableDatabase());
                activityType.post(new Runnable() {
                    @Override
                    public void run() {
                        addActivityTypes(types);
                    }
                });
            }
        }).run();
    }

    private void addActivityTypes(List<PhysicalActivityType> types) {
        activityTypes = new HashMap<>();
        for (PhysicalActivityType type : types) {
            activityTypes.put(type.getName(), type);
            RadioButton rb = new RadioButton(getContext());
            rb.setText(type.getName());
            activityType.addView(rb);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        boolean hasLocationPermissions = hasLocationPermission();
        activityStartButton.setEnabled(hasLocationPermissions);
        if (!hasLocationPermissions) {
            requestLocationPermission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onStartTrackingFitnessActivity(PhysicalActivityType activity);
    }

    public void startTrackingActivity(View view) {
        if (mListener != null) {
            int activityId = activityType.getCheckedRadioButtonId();
            RadioButton rb = activityType.findViewById(activityId);
            String activity = null;
            if (rb != null) {
                activity = rb.getText().toString();
            }
            PhysicalActivityType type = activityTypes.get(activity);
            mListener.onStartTrackingFitnessActivity(type);
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    private void requestLocationPermission() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Utils.showSnackbar(getView(), getString(R.string.location_permission_rationale), getString(android.R.string.ok),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLocationPermissionRequest();
                        }
                    });
        } else {
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    activityStartButton.setEnabled(true);
                } else {
                    // Permission denied.
                    activityStartButton.setEnabled(false);
                    // inform user that the permission is needed to use the app
                    Utils.showSnackbar(getView(), getString(R.string.location_permission_denied_explanation), getString(R.string.settings),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
            }
        }
    }

}
