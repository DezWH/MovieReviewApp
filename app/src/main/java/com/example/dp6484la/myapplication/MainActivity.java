package com.example.dp6484la.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Logging tag
    private static final String MAIN_ACTIVITY_TAG = "MAIN ACTIVITY";


    //UI Components
    Button mConfigureCurrentMovieButton, mReviewCurrentMovieButton, mClearButton;
    TextView mCurrentMovieName;
    RatingBar mCurrentMovieReview;
    CheckedTextView mSeeCurrentMovieAgain;
    TextView mInstructions;


    //Request code for Intents that lanch other Activities. Ensure they have different values.
    private final int REVIEW_ACTIVITY_REQUEST_CODE = 1;
    private final int CONFIGURE_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConfigureCurrentMovieButton = (Button) findViewById(R.id.setMovieNameButton);
        mReviewCurrentMovieButton = (Button) findViewById(R.id.reviewCurrentMovieButton);
        mClearButton= (Button) findViewById(R.id.clearButton);
        mInstructions = (TextView) findViewById(R.id.setNameIntructionTextView);

        mCurrentMovieName = (TextView) findViewById(R.id.currentMovieName);
        mCurrentMovieReview = (RatingBar) findViewById(R.id.currentMovieRatingBar);
        mSeeCurrentMovieAgain = (CheckedTextView) findViewById(R.id.currentMovieWouldSeeAgain);

        mCurrentMovieReview.setIsIndicator(true);//User can't change here, only through MovieReviewActivity
        mSeeCurrentMovieAgain.setEnabled(false); //User can't change here, only through MovieReviewActivity;


        mReviewCurrentMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchReviewActivity();
            }
        });
        mConfigureCurrentMovieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    launchConfigureActivity();

        }

    });

    mClearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    clearCurrentMovie();

        }
    });

}
        public void launchReviewActivity() {

            //Has a name been set? if no, refuse to launch the review activity

            String currentName = mCurrentMovieName.getText().toString();
            if (currentName.isEmpty()) {
                Toast.makeText(this, "Set a movie name before you can revieww it", Toast.LENGTH_LONG).show();
                return;
            }

            //Otherwise, launch the review Activity

            Intent launchReviewIntent = new Intent(MainActivity.this, MovieReviewActivity.class);

            //Provide current

            float currentStarRating = mCurrentMovieReview.getRating();
            boolean wouldSeeAgain = mSeeCurrentMovieAgain.isChecked();

            launchReviewIntent.putExtra(MovieReviewActivity.INITIAL_STAR_RATING, currentStarRating);
            launchReviewIntent.putExtra(MovieReviewActivity.INITIAL_WOULD_SEE_AGAIN, wouldSeeAgain);
            launchReviewIntent.putExtra(MovieReviewActivity.INITIAL_MOVIE_NAME, currentName);


            startActivityForResult(launchReviewIntent, REVIEW_ACTIVITY_REQUEST_CODE);
        }

        private void launchConfigureActivity(){

        Intent launchConfigureIntent = new Intent(MainActivity.this, MovieNameActivity.class);

        String currentName = mCurrentMovieName.getText().toString();

        //Send current anem as an Extra, so the MovienameActivity has
        //has an initial name to display or edit

    launchConfigureIntent.putExtra(MovieNameActivity.INTIAL_MOVIE_NAME_EXTRA, currentName);


    //Start the Activity, using the Intent, and provide a request code.
        //This will be used in onActiityResult to figure out which Activity is returning data to this Activity.

        startActivityForResult(launchConfigureIntent, CONFIGURE_ACTIVITY_REQUEST_CODE);

        }

        private void clearCurrentMovie(){

        //Clear everyting and bring back the instrucdtions
        mCurrentMovieName.setText("");
            mCurrentMovieReview.setRating(0);
        mSeeCurrentMovieAgain.setChecked(false);
        mInstructions.setVisibility(View.VISIBLE);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){

        //There are two Activities which can be started from this Activity, and both are expected to return data.
        //Use the requestCode to differentiate betwen them, and perform the appropriate task.

        if(resultCode == RESULT_OK && requestCode == REVIEW_ACTIVITY_REQUEST_CODE ) {

            //set the movie review information using the Extras in the data Intent
            //These values were in MoviewReviewActivity
            Log.d(MAIN_ACTIVITY_TAG, "onActivityResult from MovieReviewActivity");
            float review = data.getFloatExtra(MovieReviewActivity.MOVIE_STAR_RATING_EXTRA, 0);
            boolean seeAgain = data.getBooleanExtra(MovieReviewActivity.MOVIE_WOULD_SEE_AGAIN_EXTRA, false );
            mCurrentMovieReview.setRating(review);
            mSeeCurrentMovieAgain.setChecked(seeAgain);

        }

        if(resultCode == RESULT_OK && requestCode == CONFIGURE_ACTIVITY_REQUEST_CODE)
            Log.d(MAIN_ACTIVITY_TAG, "onActivityResult from MovieNameActivity");

            //set the movie name. Get from the data sent from MovieNameActivity
        String movieName = data.getStringExtra(MovieNameActivity.MOVIE_NAME_EXTRA);
        mCurrentMovieName.setText(movieName);

        //Remove instructos if name was set
        if(movieName != null && !movieName.isEmpty()){
                mInstructions.setVisibility(View.GONE);

        }

        }

        }
