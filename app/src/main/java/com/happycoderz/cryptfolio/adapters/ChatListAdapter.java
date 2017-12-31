package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.interfaces.ChatListClickListener;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatListAdapter extends ArrayAdapter<String> {

  Context context;
  ChatListClickListener listener;

  public ChatListAdapter(Context ctx, ChatListClickListener listener, List<String> arrayList) {
    super(ctx, 0, arrayList);
    this.listener = listener;
    this.context = ctx;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    String room = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(getContext())
          .inflate(R.layout.item_chat_subject, parent, false);
    }

    ChatRoomWrapper view = new ChatRoomWrapper();
    ButterKnife.bind(view, convertView);
    view.load(room);

    return convertView;
  }

  class ChatRoomWrapper {

    @BindView(R.id.name_text_view) protected TextView nameTextView;
    @BindView(R.id.join_button) protected Button joinButton;

    public void load(final String room) {
      nameTextView.setText(room);

      joinButton.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          listener.onJoinClicked(room);
        }
      });
    }
  }
}