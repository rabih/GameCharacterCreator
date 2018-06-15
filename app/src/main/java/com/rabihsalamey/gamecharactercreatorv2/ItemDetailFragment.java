package com.rabihsalamey.gamecharactercreatorv2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private CharacterContent.Stat mStat;

    private int total;

    TextView leftToSpendLabel;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mStat = CharacterContent.STAT_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStat.id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(
                        getContext().getString(R.string.app_name), Context.MODE_PRIVATE);

//        sharedPref.edit().clear().apply();
        if (mStat != null) {

            RatingBar strengthBar = ((RatingBar) rootView.findViewById(R.id.strength));
            RatingBar intellectBar = ((RatingBar) rootView.findViewById(R.id.intellect));
            RatingBar wisdomBar = ((RatingBar) rootView.findViewById(R.id.wisdom));
            RatingBar dexterityBar = ((RatingBar) rootView.findViewById(R.id.dexterity));

            strengthBar.setRating(sharedPref.getInt(mStat.id + "-strength", 0));
            intellectBar.setRating(sharedPref.getInt(mStat.id + "-intellect", 0));
            wisdomBar.setRating(sharedPref.getInt(mStat.id + "-wisdom", 0));
            dexterityBar.setRating(sharedPref.getInt(mStat.id + "-dexterity", 0));

            strengthBar.setOnRatingBarChangeListener(this);
            intellectBar.setOnRatingBarChangeListener(this);
            wisdomBar.setOnRatingBarChangeListener(this);
            dexterityBar.setOnRatingBarChangeListener(this);

            total = sharedPref.getInt("total-left-" + mStat.id, 10);

            leftToSpendLabel = ((TextView) rootView.findViewById(R.id.remaining_label));
            leftToSpendLabel.setText(total + " points left to spend.");

            //            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mStat.id);
        }

        return rootView;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(
                        getContext().getString(R.string.app_name), Context.MODE_PRIVATE);



        int valueOfOriginalRatingBar = sharedPref.getInt(mStat.id + "-" +
                getResources().getResourceEntryName(ratingBar.getId()), 0);
        Log.w("ItemDetailFragment", "Original value of " + valueOfOriginalRatingBar);

        int valueOfNewRatingBar = Math.round(rating);

        int difference = valueOfNewRatingBar - valueOfOriginalRatingBar;


        Log.w("ItemDetailFragment", "difference: " + difference + "\nTotal: " + total);
        if((total > 0 || difference < 0) && total - difference >= 0) {

            Log.i("ItemDetailFragment", "Saving " + rating + " to " + mStat.id  + "-" +
                    getResources().getResourceEntryName(ratingBar.getId()) + ".");

            total -= valueOfNewRatingBar - valueOfOriginalRatingBar;
            Log.i("ItemDetailFragment", "Stars used: " + total);
            leftToSpendLabel.setText(total + " points left to spend.");

            //save results
            sharedPref.edit().putInt(mStat.id + "-" +
                            getResources().getResourceEntryName(ratingBar.getId()),
                    Math.round(rating)).apply();

            sharedPref.edit().putInt("total-left-" + mStat.id, total).apply();


            //ratingBar.setEnabled(false);
        }
        else {
            ratingBar.setRating(valueOfOriginalRatingBar);
            Log.w("ItemDetailFragment", valueOfOriginalRatingBar +
                    ": Too many stars are in use, no more settings allowed.");
        }
    }
}
