package com.work.flow.repository.reminder;

import com.work.flow.entity.ReminderEntity;
import com.work.flow.repository.jparepo.JpaRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/*
شيء جديد عرفته بتقدر تعمل
implements and extends
بنفس الكلاس وهاي الطريقة عشان نعرف الميثود الي تحت ونكتب كونتراكت الها وتكون من الانترفيس الاصلية
نطبق مبدا Inversion of Control
يعني بدل ما نعرف الابوجكت وتبعياته لحال سويت ريبو اساسية للكل
وتمتد منها ريبو ثانية خاصة في الريمايندر
وريبو الاساسية الان بتورث العقد من الانترفيس
 */

public class JpaReminderRepo extends JpaRepository<ReminderEntity, UUID> implements ReminderRepository {

    private final EntityManager entityManager;

    public JpaReminderRepo(EntityManager entityManager) {
        super(entityManager, ReminderEntity.class);
        this.entityManager = entityManager;
    }

    @Override
    public List<ReminderEntity> findPendingReminders(LocalDateTime now) {
        /*
                استعملت كويري فيها فكرة الاينر جوين  السبب هو
                بدنا نجيب كل التذكيرات الي وقتهم للارسال لكل مستخدم
                بس السؤال كيف نفس الريمايندر بعرف المستخدم ؟؟

                هاي الطريقة بتعطينا مثل جدول فيه التذكير واليوزر وتاسك تبعه

                وعشان الاضافة برضه حطيت اندكس في نفس الانتتي للفهرسة السريعة

                بختصار بتمرر وقت بدك تشيك عليه
                بعمل استعلام جبلي الثلاث انتتي في استعلام واحد بما انه مرتبطين ببعض
                وشرط انه اللتواريخ متساوية او اقل منها بقليل مع شرط انه لم يرسل ابدا !


                String jpql = "SELECT r FROM ReminderEntity r " +
                "where r.reminderTime <= :now AND r.sent = false";

                الكويري التي فوق تعمل مع التجربة ولا يوجد بها اي مشاكل ابدا لكن
                توجد مشكلة في الخفاء اسمها N+1
                يعني في حالتي هذه يتم الاستعلام مرة واحدة ومن ثم يتم الاستعلام عن التاسك واليوزر في الخفاء
                وهذا يسبب بطئ في الاستعلامات في حال كان عدد التذكيرات كبير جدا
                وانسب حل هو استعلام واحد يجلب 3 انتتي مرة واحدة ونستعملهم

         */

        String jpql = "SELECT r FROM ReminderEntity r " +
                "JOIN FETCH r.task t " +
                "JOIN FETCH t.user u " +
                "WHERE r.reminderTime <= :now AND r.sent = false";

        // هون بعمل استعلام في الداتا بييس على حسب الكويري
        return entityManager.createQuery(jpql, ReminderEntity.class)
                .setParameter("now", now)
                .getResultList();
    }
}
