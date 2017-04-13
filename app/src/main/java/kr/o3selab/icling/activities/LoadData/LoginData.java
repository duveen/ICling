package kr.o3selab.icling.activities.loaddata;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.MainActivity;
import kr.o3selab.icling.common.GlobalApplication;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;
import kr.o3selab.icling.utils.Debug;

public class LoginData extends Fragment {

    private View mView;
    private ViewGroup mContainer;

    private ViewPager mViewPager;

    private GoogleApiClient mGoogleApiClient;

    public LoginData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login_data, null);
        mContainer = container;

        mViewPager = (ViewPager) mContainer.findViewById(R.id.load_data_view_pager);

        View googleLogin = mView.findViewById(R.id.login_google);
        googleLogin.setOnClickListener(googleLoginListener);

        return mView;
    }

    View.OnClickListener googleLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id))
                    .requestEmail()
                    .build();

            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        })
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            }

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, 9);

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 구글 로그인
        if (requestCode == 9) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("로그인 중 입니다...");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setCancelable(false);
                pd.show();

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                if (account != null) {
                    final User user = new User(User.GOOGLE, account.getDisplayName(), account.getId(), account.getEmail());
                    user.setUserProfileImage(account.getPhotoUrl().toString());
                    final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER + GlobalApplication.getUUID());
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final User dUser = dataSnapshot.getValue(User.class);
                            if (dUser != null) {
                                pd.dismiss();

                                if (!dUser.mUUID.equals(GlobalApplication.getUUID())) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("기존 가입한 기기와 실행한 기기가 다릅니다. 현재 기기로 기기를 변경하시겠습니까?");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton(R.string.default_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dUser.mUUID = GlobalApplication.getUUID();
                                            userReference.setValue(dUser);

                                            Constants.user = Constants.setLogin(dUser);
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                                    builder.setNegativeButton(R.string.default_cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity(), "아이클링은 한 개의 기기에서만 실행이 가능합니다. 프로그램을 종료합니다.", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                    });
                                    builder.show();

                                } else {
                                    Constants.user = Constants.setLogin(dUser);
                                    Toast.makeText(getContext(), "이미 가입 된 아이디가 존재합니다. 가입 된 아이디로 실행합니다.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    getActivity().finish();
                                }


                            } else {
                                pd.dismiss();
                                Constants.user = user;
                                mViewPager.setCurrentItem(1);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            pd.dismiss();
                            Debug.d(databaseError.getMessage());
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "계정 선택을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Debug.d("FirebaseAuthWithGoogle:" + acct.getId());

        FirebaseAuth auth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Debug.d("SignInWithCredential:onComplete:" + task.isSuccessful());
                    }
                });
    }
}
