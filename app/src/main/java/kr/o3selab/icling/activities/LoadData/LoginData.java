package kr.o3selab.icling.activities.LoadData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.MainActivity;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;

public class LoginData extends Fragment {

    private View mView;
    private ViewGroup mContainer;

    private ViewPager mViewPager;

    private SessionCallback mKakaoCallback;

    private GoogleApiClient mGoogleApiClient;

    public LoginData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login_data, null);
        mContainer = container;

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewPager = (ViewPager) mContainer.findViewById(R.id.load_data_view_pager);

        View kakaoLogin = mView.findViewById(R.id.login_kakao);
        kakaoLogin.setOnClickListener(kakaoLoginListener);

        View googleLogin = mView.findViewById(R.id.login_google);
        googleLogin.setOnClickListener(googleLoginListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKakaoCallback);
    }

    View.OnClickListener kakaoLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Constants.printLog("Start Kakao Login");
            if (mKakaoCallback == null) {
                mKakaoCallback = new SessionCallback();
                com.kakao.auth.Session.getCurrentSession().addCallback(mKakaoCallback);
                com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
                com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK, getActivity());
            } else {
                Constants.printLog(String.valueOf(Session.getCurrentSession().isOpened()));
            }
        }
    };

    View.OnClickListener googleLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, 9);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 9) {
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setMessage("로그인 중 입니다...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                if (account != null) {
                    final User user = new User(User.GOOGLE, account.getEmail());
                    user.mUserID = account.getId();

                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER);
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                User dUser = snap.getValue(User.class);
                                if (user.equals(dUser)) {
                                    pd.dismiss();
                                    Constants.user = dUser;
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                    break;
                                }
                            }

                            Constants.user = user;
                            mViewPager.setCurrentItem(1);
                            pd.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
            return;
        }

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Constants.printLog("Kakao Session Opened");
            kakaoRequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Constants.printLog("Kakao Session Open Failed");
            if (exception != null) {
                Constants.printLog(exception);
            }
        }
    }

    private void kakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Exception exception = errorResult.getException();
                if (exception != null) {
                    Constants.printLog(exception);
                }
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(final UserProfile result) {
                final User user = new User(User.KAKAO, String.valueOf(result.getId()));

                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(Constants.KAKAO_USER);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            User dUser = snap.getValue(User.class);
                            if (user.equals(dUser)) {
                                Constants.user = dUser;
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                                break;
                            }
                        }

                        Constants.user = user;
                        mViewPager.setCurrentItem(1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Constants.printLog("firebaseAuthWithGoogle:" + acct.getId());

        FirebaseAuth auth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Constants.printLog("signInWithCredential:onComplete:" + task.isSuccessful());
                    }
                });
    }
}
