package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.interfaces.AlertListener;
import com.happycoderz.cryptfolio.models.Alert;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

public class AlertAdapter extends RealmBaseAdapter<Alert> implements ListAdapter {

  Context context;
  AlertListener listener;

  public AlertAdapter(
      OrderedRealmCollection<Alert> realmResults, AlertListener listener, Context context
  ) {
    super(realmResults);
    this.listener = listener;
    this.context = context;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    Alert alert = getItem(position);

    if (convertView == null) {
      convertView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_list, parent, false);
    }

    AlertViewWrapper view = new AlertViewWrapper();
    ButterKnife.bind(view, convertView);
    view.load(alert);

    return convertView;
  }

  class AlertViewWrapper {

    @BindView(R.id.condition_text_view) protected TextView conditionTextView;
    @BindView(R.id.delete_image_view) protected ImageView deleteImageView;
    @BindView(R.id.bell) protected ImageView bellImageView;

    public void load(final Alert alert) {

      conditionTextView.setText(alert.getCondition());

      if (alert.isStopped()) {
        bellImageView.setImageResource(R.drawable.bell_unselected);
      } else {
        bellImageView.setImageResource(R.drawable.ic_notification);
      }

      deleteImageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Realm realm = Realm.getDefaultInstance();
          realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
              alert.deleteFromRealm();
              notifyDataSetChanged();
            }
          });
        }
      });

      bellImageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Realm realm = Realm.getDefaultInstance();
          realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
              if (alert.isStopped()) {
                alert.setStopped(false);
              } else {
                alert.setStopped(true);
              }
              notifyDataSetChanged();
            }
          });
        }
      });
    }
  }
}