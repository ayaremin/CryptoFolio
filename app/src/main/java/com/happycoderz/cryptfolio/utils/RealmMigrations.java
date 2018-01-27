package com.happycoderz.cryptfolio.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import java.util.Date;

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion < 2) {
            schema.create("Coin");
            oldVersion ++;
        }

        if (oldVersion < 3) {
            schema.create("Transaction");
            RealmObjectSchema taskSchema = schema.get("Transaction");
            // Add the complete state boolean field
            taskSchema.addField("coin", String.class);
            taskSchema.addField("date", Date.class);
            taskSchema.addField("market", String.class);
            taskSchema.addField("pair", String.class);
            taskSchema.addField("amount", double.class);
            taskSchema.addField("type", int.class);
            taskSchema.addField("buyPrice", double.class);
            oldVersion++;
        }
    }
}