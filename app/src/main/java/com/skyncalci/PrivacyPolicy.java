package com.skyncalci;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class PrivacyPolicy extends AppCompatActivity {

    private ImageView backBtn;
    private TextView descTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        bannerAd();

        backBtn=findViewById(R.id.inviteBackBtn);
        descTxt=findViewById(R.id.privacypolicyDescTxt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String webData="<html><body>" +
                "\n" +
                "<p>At SKY!N Calci, accessible from SKY!N Calci, one of our main priorities is the privacy of our visitors. This Privacy Policy document contains types of information that is collected and recorded by SKY!N Calci and how we use it.</p>\n" +
                "\n" +
                "<p>If you have additional questions or require more information about our Privacy Policy, do not hesitate to contact us.</p>\n" +
                "\n" +
                "<p>This Privacy Policy applies only to our online activities and is valid for visitors to our App with regards to the information that they shared and/or collect in SKY!N Calci. This policy is not applicable to any information collected offline or via channels other than this App.</p>\n" +
                "\n" +
                "<h2>Consent</h2>\n" +
                "\n" +
                "<p>By using our App, you hereby consent to our Privacy Policy and agree to its terms.</p>\n" +
                "\n" +
                "<h2>Information we collect</h2>\n" +
                "\n" +
                "<p>The personal information that you are asked to provide, and the reasons why you are asked to provide it, will be made clear to you at the point we ask you to provide your personal information.</p>\n" +
                "<p>If you contact us directly, we may receive additional information about you such as your name, email address, phone number, the contents of the message and/or attachments you may send us, and any other information you may choose to provide.</p>\n" +
                "<p>When you register for an Account, we may ask for your contact information, including items such as name, email address, and telephone number.</p>\n" +
                "\n" +
                "<h2>How we use your information</h2>\n" +
                "\n" +
                "<p>We use the information we collect in various ways, including to:</p>\n" +
                "\n" +
                "<ul>\n" +
                "<li>Provide, operate, and maintain our App</li>\n" +
                "<li>Improve, personalize, and expand our App</li>\n" +
                "<li>Understand and analyze how you use our App</li>\n" +
                "<li>Send you emails</li>\n" +
                "<li>Find and prevent fraud</li>\n" +
                "</ul>\n" +
                "\n" +
                "<h2>Our Advertising Partners</h2>\n" +
                "\n" +
                "<p>Some of advertisers on our App may use cookies and web beacons. Our advertising partners are listed below. Each of our advertising partners has their own Privacy Policy for their policies on user data. For easier access, we hyperlinked to their Privacy Policies below.</p>\n" +
                "\n" +
                "<ul>\n" +
                "    <li>\n" +
                "        <p>Google</p>\n" +
                "        <p><a href=\"https://policies.google.com/technologies/ads\">https://policies.google.com/technologies/ads</a></p>\n" +
                "    </li>\n" +
                "</ul>\n" +
                "\n" +
                "<h2>Advertising Partners Privacy Policies</h2>\n" +
                "\n" +
                "<P>You may consult this list to find the Privacy Policy for each of the advertising partners of SKY!N Calci.</p>\n" +
                "\n" +
                "<p>Third-party ad servers or ad networks uses technologies like cookies, JavaScript, or Web Beacons that are used in their respective advertisements and links that appear on SKY!N Calci, which are sent directly to users' app. They automatically receive your IP address when this occurs. These technologies are used to measure the effectiveness of their advertising campaigns and/or to personalize the advertising content that you see on app that you use.</p>\n" +
                "\n" +
                "<p>Note that SKY!N Calci has no access to or control over these cookies that are used by third-party advertisers.</p>\n" +
                "\n" +
                "<h2>Third Party Privacy Policies</h2>\n" +
                "\n" +
                "<p>SKY!N Calci's Privacy Policy does not apply to other advertisers or websites. Thus, we are advising you to consult the respective Privacy Policies of these third-party ad servers for more detailed information. It may include their practices and instructions about how to opt-out of certain options. </p>\n" +
                "\n" +
                "<h2>CCPA Privacy Rights (Do Not Sell My Personal Information)</h2>\n" +
                "\n" +
                "<p>Under the CCPA, among other rights, California consumers have the right to:</p>\n" +
                "<p>Request that a business that collects a consumer's personal data disclose the categories and specific pieces of personal data that a business has collected about consumers.</p>\n" +
                "<p>Request that a business delete any personal data about the consumer that a business has collected.</p>\n" +
                "<p>Request that a business that sells a consumer's personal data, not sell the consumer's personal data.</p>\n" +
                "<p>If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us.</p>\n" +
                "\n" +
                "<h2>GDPR Data Protection Rights</h2>\n" +
                "\n" +
                "<p>We would like to make sure you are fully aware of all of your data protection rights. Every user is entitled to the following:</p>\n" +
                "<p>The right to access – You have the right to request copies of your personal data. We may charge you a small fee for this service.</p>\n" +
                "<p>The right to rectification – You have the right to request that we correct any information you believe is inaccurate. You also have the right to request that we complete the information you believe is incomplete.</p>\n" +
                "<p>The right to erasure – You have the right to request that we erase your personal data, under certain conditions.</p>\n" +
                "<p>The right to restrict processing – You have the right to request that we restrict the processing of your personal data, under certain conditions.</p>\n" +
                "<p>The right to object to processing – You have the right to object to our processing of your personal data, under certain conditions.</p>\n" +
                "<p>The right to data portability – You have the right to request that we transfer the data that we have collected to another organization, or directly to you, under certain conditions.</p>\n" +
                "<p>If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us.</p>\n" +
                "\n" +
                "<h2>Children's Information</h2>\n" +
                "\n" +
                "<p>Another part of our priority is adding protection for children while using the internet. We encourage parents and guardians to observe, participate in, and/or monitor and guide their online activity.</p>\n" +
                "\n" +
                "<p>SKY!N Calci does not knowingly collect any Personal Identifiable Information from children under the age of 13. If you think that your child provided this kind of information on our website, we strongly encourage you to contact us immediately and we will do our best efforts to promptly remove such information from our records.</p></body></html>";
        Spanned convert = Html.fromHtml(webData);
        descTxt.setText(convert);
    }

    private void bannerAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        SharedPreferences sharedPreferences=getSharedPreferences("happy",MODE_PRIVATE);
        AdView mAdView = findViewById(R.id.adView);
        if (sharedPreferences.contains("fr")){
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }
    }
}
