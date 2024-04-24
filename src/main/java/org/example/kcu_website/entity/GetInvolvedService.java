package org.example.kcu_website.entity;

import org.example.kcu_website.model.GetInvolved;
import org.example.kcu_website.repository.GetInvolvedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class GetInvolvedService {
    @Autowired
    private GetInvolvedRepository getInvolvedRepository;

    public Optional<GetInvolved> findGetInvolvedPeriod() {
        return getInvolvedRepository.findFirstByOrderByStartDateDesc();
    }

    public boolean isWithinGetInvolvedPeriod(String startDate, String endDate) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);

        LocalDate now = LocalDate.now();
        return !(now.isBefore(startLocalDate) || now.isAfter(endLocalDate));
    }
}
