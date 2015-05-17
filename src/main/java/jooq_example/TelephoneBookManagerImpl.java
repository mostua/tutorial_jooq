package jooq_example;

import jooq_sources.Sequences;
import jooq_sources.Tables;
import jooq_sources.tables.daos.FriendDao;
import jooq_sources.tables.daos.PhoneNumberDao;
import jooq_sources.tables.pojos.Friend;
import jooq_sources.tables.pojos.PhoneNumber;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Adam Mościcki
 */
public class TelephoneBookManagerImpl implements TelephoneBookManager{

    private final Configuration configuration;
    private final FriendDao friendDao;
    private final PhoneNumberDao phoneNumberDao;

    @Autowired
    public TelephoneBookManagerImpl(Configuration configuration) {
        this.configuration = configuration;
        this.friendDao = new FriendDao(configuration);
        this.phoneNumberDao = new PhoneNumberDao(configuration);
    }

    @Override
    public List<Friend> getFriends() {
        return friendDao.findAll();
    }

    @Override
    public void addFriend(Friend friend) {
        //addFriendByDirectlySet(friend);
        //addFriendByDao(friend);
        addFriendDirectlySimple(friend);
    }

    private void addFriendByDao(Friend friend) {
        DSLContext create = DSL.using(configuration);
        Long nextId = create.nextval(Sequences.FRIEND_ID_SEQ);
        friend.setId(nextId.intValue());
        friendDao.insert(friend);
    }

    private void addFriendByDirectlySet(Friend friend) {
        DSLContext create = DSL.using(configuration);
        create.insertInto(Tables.FRIEND)
                .set(Tables.FRIEND.ID, Sequences.FRIEND_ID_SEQ.nextval().cast(Tables.FRIEND.ID))
                .set(Tables.FRIEND.NAME, friend.getName())
                .set(Tables.FRIEND.SURNAME, friend.getSurname())
                .execute();
    }

    private void addFriendDirectlySimple(Friend friend) {
        DSLContext create = DSL.using(configuration);
        create.insertInto(Tables.FRIEND)
                .values(Sequences.FRIEND_ID_SEQ.nextval(), friend.getName(), friend.getSurname())
                .execute();
    }

    @Override
    public void addFriendWithPhoneNumber(Friend friend, String phoneNumber) {
        addFriendWithPhoneNumberClassic(friend, phoneNumber);
        //addFriendWithPhoneNumberLambda(friend, phoneNumber);
    }

    private void addFriendWithPhoneNumberLambda(Friend friend, String phoneNumber) {
        DSLContext create = DSL.using(configuration);
        create.transaction(configuration -> {
                DSLContext createInner = DSL.using(configuration);
                Integer id = createInner.insertInto(Tables.FRIEND)
                        .values(Sequences.FRIEND_ID_SEQ.nextval(), friend.getName(), friend.getSurname())
                        .returning(Tables.FRIEND.ID).fetchOne().getId();
                createInner.insertInto(Tables.PHONE_NUMBER)
                        .values(Sequences.PHONE_NUMBER_ID_SEQ.nextval(), phoneNumber).execute();
        });
    }

    private void addFriendWithPhoneNumberClassic(final Friend friend, final String phoneNumber) {
        DSLContext create = DSL.using(configuration);
        create.transaction(new TransactionalRunnable() {
            @Override
            public void run(Configuration configuration) throws Exception {
                DSLContext create = DSL.using(configuration);
                Integer id = create.insertInto(Tables.FRIEND)
                        .values(Sequences.FRIEND_ID_SEQ.nextval(), friend.getName(), friend.getSurname())
                        .returning(Tables.FRIEND.ID).fetchOne().getId();
                create.insertInto(Tables.PHONE_NUMBER)
                        .values(Sequences.PHONE_NUMBER_ID_SEQ.nextval(), phoneNumber, id).execute();
            }
        });
    }

    @Override
    public void removeFriend(Integer friendId) {
        DSLContext create = DSL.using(configuration);
        create.transaction(configuration -> {
            DSLContext createInner = DSL.using(configuration);
            createInner.delete(Tables.PHONE_NUMBER)
                .where(Tables.PHONE_NUMBER.FRIEND_ID.equal(friendId))
                .execute();
            friendDao.deleteById(friendId);
        });
    }

    @Override
    public List<PhoneNumber> getFriendPhones(Integer friendId) {
        return phoneNumberDao.fetchByFriendId(friendId);
    }

    @Override
    public void addPhoneToFriend(Integer friendId, String phoneNumber) {
        phoneNumberDao.insert(new PhoneNumber(DSL.using(configuration).nextval(Sequences.PHONE_NUMBER_ID_SEQ).intValue(),
                                                phoneNumber, friendId));
    }

    @Override
    public void removeNumber(Integer phoneId) {
        phoneNumberDao.deleteById(phoneId);
    }
}
