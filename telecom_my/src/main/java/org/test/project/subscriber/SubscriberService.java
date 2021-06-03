package org.test.project.subscriber;

import lombok.RequiredArgsConstructor;
import org.test.project.subscriber.dto.SubscriberCreateDTO;
import org.test.project.subscriber.dto.SubscriberReplenishDTO;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SubscriberMapper subscriberMapper;

    public Subscriber create(SubscriberCreateDTO subscriberCreateDTO) {
        SubscriberCreateDTO dto = subscriberRepository.insertSubscriber(subscriberCreateDTO);
        return subscriberMapper.getSubscriberFromCreateDTO(dto);
    }

    public Subscriber getSubscriberById(Long id) {
        return subscriberRepository.getById(id).orElseThrow(() -> new SubscriberException("subscriber with id: "
                + id + " doesn't exist"));
    }

    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    public Subscriber lockSubscriberById(Long id) {
        return subscriberRepository.lockSubById(id);
    }

    public Subscriber unlockSubscriberById(Long id) {
        return subscriberRepository.unlockSubById(id);
    }

    public Subscriber replenishBalance(SubscriberReplenishDTO replenishDTO, Double amount) {
        Subscriber subscriberBeforeReplenish = getSubscriberById(replenishDTO.getId());
        double balanceBefore = subscriberBeforeReplenish.getBalance();
        replenishDTO.setBalance(getNewBalance(balanceBefore, amount));
        subscriberRepository.replenishBalanceById(replenishDTO);
        Subscriber subscriberAfterReplenish = subscriberMapper.getSubscriberFromReplenishDTO(replenishDTO);
        if (balanceBefore < 0 && replenishDTO.getBalance() >= 0) {
            Subscriber unlockSubById = subscriberRepository.unlockSubById(subscriberAfterReplenish.getId());
            subscriberAfterReplenish.setLock(unlockSubById.isLock());
            subscriberAfterReplenish.setLogin(subscriberBeforeReplenish.getLogin());
        }
        return subscriberAfterReplenish;
    }

    private double getNewBalance(Double currentBalance, Double amount) {
        BigDecimal currBalance = BigDecimal.valueOf(currentBalance);
        BigDecimal amountBigDec = BigDecimal.valueOf(amount);
        return Double.parseDouble(currBalance.add(amountBigDec).toString());
    }

    public Subscriber getSubscriberByLogin(String login) {
        return subscriberRepository.getByLogin(login).orElseThrow(
                () -> new SubscriberException("Subscriber with login: " + login + " not found"));

    }
}
