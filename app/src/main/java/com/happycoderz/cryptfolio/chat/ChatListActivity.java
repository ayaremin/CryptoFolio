package com.happycoderz.cryptfolio.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.types.AccountDetails;
import co.chatsdk.ui.manager.InterfaceManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.happycoderz.cryptfolio.BaseActivity;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.ChatListAdapter;
import com.happycoderz.cryptfolio.interfaces.ChatListClickListener;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.util.Arrays;
import java.util.List;

import static co.chatsdk.core.types.AccountDetails.signUp;

public class ChatListActivity extends BaseActivity implements ChatListView, ChatListClickListener {

  @InjectPresenter ChatListPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) ChatListPresenter provideContext() {
    ChatListPresenter presenter = new ChatListPresenter();
    presenter.setContext(this);
    return presenter;
  }

  @BindView(R.id.list_view) protected ListView listView;

  private ChatListAdapter adapter;
  private CacheHelper cacheHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_list);
    ButterKnife.bind(this);
    presenter.start();
    cacheHelper = CacheHelper.getInstance(this);
    String[] myResArray = getResources().getStringArray(R.array.chatrooms);
    List<String> chatRooms = Arrays.asList(myResArray);

    adapter = new ChatListAdapter(this, this, chatRooms);
    listView.setAdapter(adapter);
  }

  @Override public void onJoinClicked(final String room) {
    if (!NM.auth().userAuthenticated()) {
      new MaterialDialog.Builder(this).title(R.string.label_ask_username)
          .content(R.string.label_join_chat)
          .inputType(InputType.TYPE_CLASS_TEXT)
          .input("", "", new MaterialDialog.InputCallback() {
            @Override public void onInput(MaterialDialog dialog, final CharSequence input) {
              if (TextUtils.isEmpty(input.toString())) return;

              AccountDetails details = signUp(input.toString() + "@cryptofolio.com",
                  String.valueOf(input.toString().hashCode()));
              showProgress();
              NM.auth()
                  .authenticate(details)
                  .observeOn(AndroidSchedulers.mainThread())
                  .doFinally(new Action() {
                    @Override public void run() throws Exception {
                      hideProgress();
                    }
                  })
                  .subscribe(new Action() {
                    @Override public void run() throws Exception {
                      hideProgress();
                      Answers.getInstance()
                          .logContentView(new ContentViewEvent().putContentName("ChatRoom")
                              .putContentType("Clicked")
                              .putCustomAttribute("firstTime", "true")
                              .putCustomAttribute("which", room)
                              .putCustomAttribute("who", input.toString()));
                      InterfaceManager.shared().a.startChatActivityForID(
                          ChatListActivity.this, room);
                    }
                  }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable e) throws Exception {
                      e.printStackTrace();
                      showError(e.getMessage());
                    }
                  });
            }
          })
          .show();
      return;
    }
    showProgress();
    NM.auth()
        .authenticateWithCachedToken()
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally(new Action() {
          @Override public void run() throws Exception {

          }
        })
        .subscribe(new Action() {
          @Override public void run() throws Exception {
            hideProgress();
            Answers.getInstance()
                .logContentView(new ContentViewEvent().putContentName("ChatRoom")
                    .putContentType("Clicked")
                    .putCustomAttribute("firstTime", "false")
                    .putCustomAttribute("which", room)
                    .putCustomAttribute("who", NM.currentUser().getEmail()));
            InterfaceManager.shared().a.startChatActivityForID(ChatListActivity.this, room);
            /*NM.publicThread().createPublicThreadWithName(room,room).observeOn(AndroidSchedulers
                .mainThread())
                .subscribe();*/
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(Throwable e) throws Exception {
            e.printStackTrace();
          }
        });
  }

  @OnClick(R.id.share_image_view) protected void onShareClicked() {
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CryptoFolio");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
        getString(R.string.label_download, BuildConfig.APPLICATION_ID));
    startActivity(Intent.createChooser(sharingIntent, "Share via"));
  }

  @OnClick(R.id.back_image_view) protected void onBackClicked() {
    finish();
  }
}
