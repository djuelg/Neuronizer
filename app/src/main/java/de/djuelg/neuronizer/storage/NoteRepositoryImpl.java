package de.djuelg.neuronizer.storage;

import android.support.annotation.NonNull;

import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.repository.NoteRepository;
import de.djuelg.neuronizer.storage.converter.NoteDAOConverter;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.model.NoteDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static de.djuelg.neuronizer.storage.RepositoryManager.createConfiguration;

/**
 * Created by dmilicic on 1/29/16.
 */
public class NoteRepositoryImpl implements NoteRepository {

    private final RealmConfiguration configuration;

    public NoteRepositoryImpl(String realmName) {
        this.configuration = createConfiguration(realmName);
    }

    // RealmConfiguration injectable for testing
    NoteRepositoryImpl(RealmConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public List<Note> getAll() {
        Realm realm = Realm.getInstance(configuration);
        RealmResults<NoteDAO> noteDAOs = realm.where(NoteDAO.class).findAll();
        List<Note> notes = new ArrayList<>(noteDAOs.size());
        for (NoteDAO dao : noteDAOs) {
            notes.add(RealmConverter.convert(dao));
        }
        realm.close();
        return notes;
    }

    @Override
    public Optional<Note> get(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        Optional<NoteDAO> noteDAO = Optional.fromNullable(realm.where(NoteDAO.class).equalTo("uuid", uuid).findFirst());
        Optional<Note> note = noteDAO.transform(new NoteDAOConverter());
        realm.close();
        return note;

    }

    @Override
    public boolean insert(Note note) {
        Realm realm = Realm.getInstance(configuration);
        final NoteDAO dao = RealmConverter.convert(note);

        realm.beginTransaction();
        try {
            realm.copyToRealm(dao);
            realm.commitTransaction();
        } catch (Throwable throwable) {
            realm.cancelTransaction();
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }

    @Override
    public void update(Note updatedNote) {
        Realm realm = Realm.getInstance(configuration);

        final NoteDAO noteDAO = RealmConverter.convert(updatedNote);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(noteDAO);
            }
        });
        realm.close();
    }

    @Override
    public void delete(final Note deletedNote) {
        Realm realm = Realm.getInstance(configuration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                NoteDAO dao = realm.where(NoteDAO.class).equalTo("uuid", deletedNote.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
        realm.close();
    }
}
