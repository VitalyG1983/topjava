package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public abstract class AbstractStorage<SK> implements Storage {
    static final Comparator<Meal> RESUME_NAME_COMPARATOR = Comparator.comparing(Meal::getDateTime);
    public static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(AbstractStorage.class);

    protected abstract SK getSearchKey(Integer id);

    protected abstract void doSave(Meal meal, SK searchKey);

    protected abstract void doUpdate(Meal meal, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract Meal doGet(SK searchKey);

    protected abstract boolean isExist(SK searchKey);

    protected abstract List<Meal> getStorage();

    public List<Meal> getAllSorted() {
        log.info("getAllSorted()");
        List<Meal> mealList = getStorage();
        mealList.sort(RESUME_NAME_COMPARATOR);
        return mealList;
    }

    public void save(Meal m) {
        log.info("Save " + m);
        SK searchKey = getNotExistedSearchKey(m.getId());
        doSave(m, searchKey);

    }

    public void delete(Integer id) {
        log.info("Delete" + id);
        SK searchKey = getExistedSearchKey(id);
        doDelete(searchKey);
        System.out.println("Meal with id=" + id + " deleted ");
    }

    public void update(Meal meal) {
        log.info("Update" + meal);
        SK searchKey = getExistedSearchKey(meal.getId());
        // meal founded, -> save new meal instead old
        doUpdate(meal, searchKey);
        System.out.println("Meal with id=" + meal.getId() + " updated in Database");
    }

    public Meal get(Integer id) {
        log.info("Get" + id);
        SK searchKey = getExistedSearchKey(id);
        return doGet(searchKey);
    }

    private SK getNotExistedSearchKey(Integer id) {
        SK searchKey = getSearchKey(id);
        if (isExist(searchKey)) {
            log.warn("Meal " + id + " already exist");
            //throw new ExistStorageException(id);
        }
        return searchKey;
    }

    private SK getExistedSearchKey(Integer id) {
        SK searchKey = getSearchKey(id);
        if (!isExist(searchKey)) {
            log.warn("Meal " + id + " not exist");
            //throw new NotExistStorageException(id);
        }
        return searchKey;
    }
}