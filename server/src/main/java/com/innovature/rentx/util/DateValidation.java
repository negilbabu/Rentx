package com.innovature.rentx.util;

import com.innovature.rentx.entity.OrderProduct;
import com.innovature.rentx.entity.Product;
import com.innovature.rentx.exception.BadRequestException;
import com.innovature.rentx.json.Json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;


@Component
public class DateValidation {
    @Autowired
    private LanguageUtil languageUtil;

    @Autowired
    private ProductUtil productUtil;

    private final EntityManager entityManager;

    private static final String DATE_FORMAT = "yyyy-MM-dd";


    public DateValidation(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void dateCheck(Date startDate, Date endDate) {

        Date currentDate = new Date();
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String curdate = dateFormat.format(currentDate);
        try {
            dt = dateFormat.parse(curdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int countStart = startDate.compareTo(dt);

        int btwStartEnd = endDate.compareTo(startDate);

        if (countStart < 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("start.date.validation", null, "en"));
        }

        if (btwStartEnd < 0) {
            throw new BadRequestException(languageUtil.getTranslatedText("btw.start.end.date", null, "en"));
        }

    }

    public boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public void verifyDateExists(String dates,String errorMessage){
        // Parse the date string to LocalDate
        try {
       LocalDate.parse(dates);
        } catch (Exception e){
            throw new BadRequestException(languageUtil.getTranslatedText(errorMessage, null, "en"));

        }
    }

    public Date stringToDateFormat(String stringDate,String errorMessage) {

        try {
            return new SimpleDateFormat(Json.DATE_FORMAT).parse(stringDate);

            } catch (ParseException e) {
            throw new BadRequestException(languageUtil.getTranslatedText(errorMessage, null, "en"));

        }
    }
    
    public int dateDifferenceCheck(Date startDate, Date endDate){
        return endDate.compareTo(startDate);
    }
    public int checkDateDifferenceInMillis(Date startDate, Date endDate){
        long millisecondsDifference = endDate.getTime() - startDate.getTime();
        int dateDifference=(int) TimeUnit.MILLISECONDS.toDays(millisecondsDifference);
        return dateDifference+1;
    }


    public Boolean productAvailabilityCheck(Product product, Date startDate, Date endDate, Integer quantity){
            int totalOrderedQuantity = 0;
            Boolean check= DateCheckForCartListWithOutErrorThrow(startDate, endDate);
            if(Boolean.TRUE.equals(check)) {
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
                Root<OrderProduct> root = criteriaQuery.from(OrderProduct.class);

                Predicate startDatePredicate = criteriaBuilder.between(root.get("startDate"), startDate, endDate);
                Predicate endDatePredicate = criteriaBuilder.between(root.get("endDate"), startDate, endDate);
                Predicate productIdPredicate = criteriaBuilder.equal(root.get("product").get("id"), product.getId());
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), 0);

                criteriaQuery.select(criteriaBuilder.sum(root.get("quantity"))).where(
                        startDatePredicate,
                        endDatePredicate,
                        productIdPredicate,
                        statusPredicate
                );

                TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
                Number totalOrderedQuantityResult = query.getSingleResult();
                if (totalOrderedQuantityResult != null) {
                    totalOrderedQuantity = totalOrderedQuantityResult.intValue();
                }

                int totalAvailableStock = product.getAvailableStock() - totalOrderedQuantity;
                return totalAvailableStock >= quantity;
            }
            else{
                return false;
            }
    }

    public Integer productAvailabilityCheckNoAuth(Integer productId, Date startDate, Date endDate, Integer quantity){


        int totalOrderedQuantity = 0;
        dateCheck(startDate, endDate);
        Product product=productUtil.validateProduct(productId);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
        Root<OrderProduct> root = criteriaQuery.from(OrderProduct.class);

        Predicate startDatePredicate = criteriaBuilder.between(root.get("startDate"), startDate, endDate);
        Predicate endDatePredicate = criteriaBuilder.between(root.get("endDate"), startDate, endDate);
        Predicate productIdPredicate = criteriaBuilder.equal(root.get("product").get("id"), product.getId());
        Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), 0);

        criteriaQuery.select(criteriaBuilder.sum(root.get("quantity"))).where(
                startDatePredicate,
                endDatePredicate,
                productIdPredicate,
                statusPredicate
        );

        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
        Number totalOrderedQuantityResult = query.getSingleResult();
        if (totalOrderedQuantityResult != null) {
            totalOrderedQuantity = totalOrderedQuantityResult.intValue();
        }

        return product.getAvailableStock() - totalOrderedQuantity;

    }

    public String startDateToString(String startDate){
        String formattedStartDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (startDate == null || startDate.equals("null")) {
            Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            formattedStartDate = dateFormat.format(currentDate);
        } else {
            formattedStartDate = startDate;
        }
        return formattedStartDate;
    }
    public String endDateToString(String endDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String formattedEndDate;
        if (endDate == null|| endDate.equals("null")) {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            Date nextDate = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
            formattedEndDate = dateFormat.format(nextDate);
        } else {
            formattedEndDate = String.valueOf(endDate);
        }
        return formattedEndDate;
    }

    public Boolean DateCheckForCartListWithOutErrorThrow(Date startDate, Date endDate) {

        Date currentDate = new Date();
        Date dt = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String curDate = dateFormat.format(currentDate);
        try {
            dt = dateFormat.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int countStart = startDate.compareTo(dt);

        int btwStartEnd = endDate.compareTo(startDate);

        return countStart >= 0 && btwStartEnd >= 0;

    }
}
