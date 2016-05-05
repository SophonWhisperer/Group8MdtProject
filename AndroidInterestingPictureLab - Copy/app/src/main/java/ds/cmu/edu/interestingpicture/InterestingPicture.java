package ds.cmu.edu.interestingpicture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class InterestingPicture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding a picture from Flickr, it
         * can callback to this object with the resulting picture Bitmap.  The "this" of the OnClick will be the OnClickListener, not
         * this InterestingPicture.
         */
        final InterestingPicture ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */

        process(ma);




    }

    public void process(final InterestingPicture ma){
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                setContentView(R.layout.activity_main);
                String searchTerm = "";
                GetPicture gp = new GetPicture();
                gp.search(searchTerm, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
                setContentView(R.layout.activity_main);
                Button backButton = (Button) findViewById(R.id.back);
                // Add a listener to the send button
                backButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View viewParam) {
                        setContentView(R.layout.home);
                        process(ma);
                    }
                });

            }
        });

    }

    /*
     * This is called by the GetPicture object when the picture is ready.  This allows for passing back the Bitmap picture for updating the ImageView
     */
    public void pictureReady(Recipe[] r) {
        String s = "";
        ImageView pictureView;
        TextView titleView;
        TextView urlView;
        int num = r.length;
        if(num<6)
            num = num;
        else
            num =5;
        for(int i =0;i<num;i++) {
            switch(i) {
                case 0:
                    pictureView = (ImageView) findViewById(R.id.interestingPicture1);
                    titleView = (TextView) findViewById(R.id.textView1);
                    urlView = (TextView) findViewById(R.id.link1);
                    break;
                case 1:
                    pictureView = (ImageView) findViewById(R.id.interestingPicture2);
                    titleView = (TextView) findViewById(R.id.textView2);
                    urlView = (TextView) findViewById(R.id.link2);
                    break;
                case 2:
                    pictureView = (ImageView) findViewById(R.id.interestingPicture3);
                    titleView = (TextView) findViewById(R.id.textView3);
                    urlView = (TextView) findViewById(R.id.link3);
                    break;
                case 3:
                    pictureView = (ImageView) findViewById(R.id.interestingPicture4);
                    titleView = (TextView) findViewById(R.id.textView4);
                    urlView = (TextView) findViewById(R.id.link4);
                    break;
                case 4:
                    pictureView = (ImageView) findViewById(R.id.interestingPicture5);
                    titleView = (TextView) findViewById(R.id.textView5);
                    urlView = (TextView) findViewById(R.id.link5);
                    break;
                default:
                    pictureView = (ImageView) findViewById(R.id.interestingPicture4);
                    titleView = (TextView) findViewById(R.id.textView4);
                    urlView = (TextView) findViewById(R.id.link5);
                    break;
            }
            if (r[i].getDish() != null && r[i].getTitle()!=null) {
                pictureView.setImageBitmap(r[i].getDish());
                titleView.setText(r[i].getTitle());
                urlView.setText(r[i].getRecipeUrl());
                urlView.setVisibility(View.VISIBLE);
                titleView.setVisibility(View.VISIBLE);
                pictureView.setVisibility(View.VISIBLE);
            } else {
                pictureView.setImageResource(R.mipmap.ic_launcher);
                pictureView.setVisibility(View.INVISIBLE);
            }
            pictureView.invalidate();
        }
    }
}
