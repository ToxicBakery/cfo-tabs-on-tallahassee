package com.ToxicBakery.app.tabsontallahassee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.app.tabsontallahassee.api.retrofit.RetrofitService;
import com.ToxicBakery.app.tabsontallahassee.api.retrofit.TabsAPI;
import com.ToxicBakery.app.tabsontallahassee.db.DetailedPersonDB;
import com.ToxicBakery.app.tabsontallahassee.db.MembershipDB;
import com.ToxicBakery.app.tabsontallahassee.db.OrganizationDetailDB;
import com.ToxicBakery.app.tabsontallahassee.db.PersonDB;
import com.ToxicBakery.app.tabsontallahassee.models.Contact;
import com.ToxicBakery.app.tabsontallahassee.models.Membership;
import com.ToxicBakery.app.tabsontallahassee.models.MembershipDataWrapper;
import com.ToxicBakery.app.tabsontallahassee.models.Person;
import com.ToxicBakery.app.tabsontallahassee.models.PersonAttributes;
import com.ToxicBakery.app.tabsontallahassee.models.PersonDataWrapper;
import com.ToxicBakery.app.tabsontallahassee.recycler.DividerItemDecoration;
import com.ToxicBakery.app.tabsontallahassee.recycler.PersonDetailAdapter;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityPerson extends AppCompatActivity {

    public static final String EXTRA_UUID = "EXTRA_UUID";

    private static final String TAG = "ActivityPerson";

    private TabsAPI tabsAPI;
    private DetailedPersonDB detailedPersonDB;
    private MembershipDB membershipDB;
    private String uuid;
    private Subscription subscription;
    private PersonDetailAdapter personDetailAdapter;
    private Set<Subscription> organizationAndMembershipSubscriptions;

    private ImageView personImage;
    private TextView personName;
    private TextView personResidence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        organizationAndMembershipSubscriptions = new LinkedHashSet<>();

        personImage = (ImageView) findViewById(R.id.person_image);
        personName = (TextView) findViewById(R.id.person_name);
        personResidence = (TextView) findViewById(R.id.person_residence);

        tabsAPI = RetrofitService.getInstance()
                .getTabsApi();

        detailedPersonDB = DetailedPersonDB.getInstance(getApplicationContext());
        membershipDB = MembershipDB.getInstance(getApplicationContext());
        OrganizationDetailDB organizationDetailDB = OrganizationDetailDB.getInstance(getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, null);

        personDetailAdapter = new PersonDetailAdapter(organizationDetailDB);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(personDetailAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Intent intent = getIntent();
        if (intent != null) {
            uuid = intent.getStringExtra(EXTRA_UUID);
        }

        if (uuid != null) {
            loadPerson();

            Person person = PersonDB.getInstance(getApplicationContext())
                    .getById(uuid);

            updateView(person);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (subscription != null) {
            subscription.unsubscribe();
        }

        for (Subscription subscription : organizationAndMembershipSubscriptions) {
            subscription.unsubscribe();
        }

    }

    void updateView(Person person) {
        PersonAttributes personAttributes = person.getPersonAttributes();
        Contact[] contacts = personAttributes.getContacts();

        personName.setText(personAttributes.getName().replace(", ", "\n"));

        Picasso.with(this)
                .load(personAttributes.getImage())
                .placeholder(R.drawable.blank_profile_image)
                .into(personImage);

        PersonAttributes.PersonExtras personExtras = personAttributes.getExtras();
        if (personExtras != null) {
            String residence = getString(R.string.person_residence_since, personExtras.getResidence(), personExtras.getMemberSince());
            personResidence.setText(residence);
        } else {
            personResidence.setText("");
        }

        String[] membershipIds = person.getMemberships();
        List<Membership> memberships = new LinkedList<>();
        for (String id : membershipIds) {
            Membership membership = membershipDB.getById(id);
            if (membership == null) {
                loadMembership(id);
            } else {
                memberships.add(membership);
            }
        }

        personDetailAdapter.set(contacts,
                memberships.toArray(new Membership[memberships.size()]));

    }

    void notifyPersonLoaded() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Person person = detailedPersonDB.getById(uuid);
                updateView(person);
            }
        });
    }

    void loadPerson() {
        Person person = detailedPersonDB.getById(uuid);
        if (person == null) {
            loadPersonFromNetwork();
        } else {
            notifyPersonLoaded();
        }
    }

    void loadPersonFromNetwork() {
        subscription = tabsAPI.getPersonDetailed(uuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonDataWrapper>() {

                    @Override
                    public void onCompleted() {
                        notifyPersonLoaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Failed to load detailed person", e);
                    }

                    @Override
                    public void onNext(PersonDataWrapper personDataWrapper) {
                        Person person = personDataWrapper.getData();
                        detailedPersonDB.addOrReplace(person);
                    }

                });
    }

    void loadMembership(String uuid) {
        Subscription subscription = tabsAPI.getMembership(uuid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MembershipDataWrapper>() {
                    @Override
                    public void onCompleted() {
                        notifyPersonLoaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Failed to load membership.", e);
                    }

                    @Override
                    public void onNext(MembershipDataWrapper membershipDataWrapper) {
                        Membership membership = membershipDataWrapper.getData();
                        membershipDB.addOrReplace(membership);
                    }
                });

        organizationAndMembershipSubscriptions.add(subscription);
    }

}
