# Day 4 Mission

🔗 이 문서는 [인프런 워밍업 클럽 스터디 2기 - 백엔드 클린 코드, 테스트 코드](https://www.inflearn.com/course/offline/warmup-club-2-be-wb) 의 진도에 따라 4일차 미션을 수행한 문서입니다.
[Readable Code: 읽기 좋은 코드를 작성하는 사고법](https://www.inflearn.com/course/readable-code-%EC%9D%BD%EA%B8%B0%EC%A2%8B%EC%9D%80%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1%EC%82%AC%EA%B3%A0%EB%B2%95/dashboard) 강의 내용을 바탕으로 작성되었습니다.

# 🛠️ 코드 리팩토링
> 아래 코드와 설명을 보고, [섹션 3. 논리, 사고의 흐름]에서 이야기하는 내용을 중심으로 읽기 좋은 코드로 리팩토링해 봅시다.

```java
public boolean validateOrder(Order order) {
    if (order.getItems().size() == 0) {
        log.info("주문 항목이 없습니다.");
        return false;
    } else {
        if (order.getTotalPrice() > 0) {
            if (!order.hasCustomerInfo()) {
                log.info("사용자 정보가 없습니다.");
                return false;
            } else {
                return true;
            }
        } else if (!(order.getTotalPrice() > 0)) {
            log.info("올바르지 않은 총 가격입니다.");
            return false;
        }
    }
    return true;
}
```

```
✔️ 사용자가 생성한 '주문'이 유효한지를 검증하는 메서드. 
✔️ Order는 주문 객체이고, 필요하다면 Order에 추가적인 메서드를 만들어도 된다. (Order 내부의 구현을 구체적으로 할 필요는 없다.) 
✔️ 필요하다면 메서드를 추출할 수 있다.
```

![brain](images/img.png)

## Early Return으로 리팩토링

위 코드를 살펴보면, `if`문과 `else`, `else if`문의 조합으로 뇌 메모리에 올려야 할 내용이 너무 많습니다.
꼭 청기 백기 게임을 하듯 이거 하면 이거 하지 말고 이거 해,.. 하는 코드는 가독성을 해치고 우리의 뇌를 고통스럽게 만듭니다.
긴 코드지만, `validateOrder(Order order)`에서 검증하고 있는 내용은 크게 세 가지 입니다.

```
1. 주문 항목이 없는 경우
2. 올바르지 않은 총 가격을 가지고 있는 경우(총 가격이 0보다 작은 경우)
3. 사용자 정보가 없는 경우
```

따라서 먼저 빠른 `return`을 통해 `else`문을 줄이도록 리팩토링 해보겠습니다.

```java
public boolean validateOrder(Order order) {
    if (order.getItems().size() == 0) {
        log.info("주문 항목이 없습니다.");
        return false;
    }

    if (!(order.getTotalPrice() > 0)) {
        log.info("올바르지 않은 총 가격입니다.");
        return false;
    }

    if (!order.hasCustomerInfo()) {
        log.info("사용자 정보가 없습니다.");
        return false;
    }
    return true;
}
```

이제 굳이 다른 조건을 뇌 메모리에 저장할 필요 없이, 각각의 조건을 모두 통과 했을 때 `true`를 반환하게 됩니다.

## 부정어 삭제로 리팩토링

그럼 이번엔 이 두 메서드에 주목해 봅시다.

```java
    if (!(order.getTotalPrice() > 0)) {
        log.info("올바르지 않은 총 가격입니다.");
        return false;
    }

    if (!order.hasCustomerInfo()) {
        log.info("사용자 정보가 없습니다.");
        return false;
    }
```

`!(order.getTotalPrice() > 0)`는 실질적으로 `order.getTotalPrice() < 0`과 같습니다.

`!order.hasCustomerInfo()` 또한 `order.hasNotCustomerInfo()`와 같습니다.

`!` 연산자를 사용하게 되면서 우리는 사고를 한 번 뒤집어주어야 합니다.

뇌 메모리를 낭비하지 않기 위해 **부정어를 삭제** 해줍니다.

```java
        if (order.getTotalPrice() < 0) {
            log.info("올바르지 않은 총 가격입니다.");
            return false;
        }

        if (order.hasNotCustomerInfo()) {
            log.info("사용자 정보가 없습니다.");
            return false;
        }
```

## 예외 처리로 리팩토링

`return false`를 한다는 것은 `validateOrder(Order order)` 메서드를 사용하는 어느 곳에선가 결괏값을 다시 처리해주어야 함을 의미합니다.
`false`라는 값 말고, `ValidateException` 클래스를 만들어 던져주면 어떨까요?
**주문 객체가 프로그램이 원하는 값과 다르다**라는 것, **의도적으로 발생시킨 예외라는 것**을 `false` 값을 넘겨줄 때보다 확실하게 표현할 수 있습니다.

```java
public class ValidateException extends IllegalArgumentException {
    public ValidateException(String message) {
        super(message);
    }
}
```

```java
public boolean validateOrder(Order order) {
        if (order.getItems().size() == 0) {
            throw new ValidateException("주문 항목이 없습니다.");
        }

        if (order.getTotalPrice() < 0) {
            throw new ValidateException("올바르지 않은 총 가격입니다.");
        }

        if (order.hasNotCustomerInfo()) {
            throw new ValidateException("사용자 정보가 없습니다.");
        }
        return true;
    }
```

# 🚧 SOLID
> SOLID에 대해 자기만의 언어로 정리해 봅시다.

## SRP: Single Responsibility Principle
- 객체는 하나의 책임만 가져야 하며, 클래스가 변경되는 이유는 하나뿐이어야 합니다.
- 객체는 맡은 책임을 수행하기 위한 행동들을 추상화한 것이어야 합니다.
- 하나의 객체가 여러 책임을 가지면 그 중 하나의 책임이 변경될 때 해당 객체가 영향을 받아 다른 기능에도 영향을 미칩니다.
- 스마트폰 안에 있는 각 앱들을 생각해봅시다. 카메라 앱은 사진을 찍는 데만 책임이 있고, 메시지 앱은 메시지를 보내는 데만 책임이 있습니다. 만약 음악 앱이 사진을 찍는 기능을 가지고 있거나, 운동 앱이 메시지를 보내는 기능을 가지고 있다면 앱이 복잡해지고 유지보수가 어려워집니다.

## OCP: Open-Closed Principle
- 객체는 확장에 열려 있어야 하고, 수정에 닫혀 있어야 합니다.
  - 새로운 기능의 추가나 변경 사항이 생겼을 때, 기존 코드는 확장할 수 있어야 합니다.
  - 기존의 코드는 수정되지 않아야 합니다.
- OCP를 지키며 설계하면 변경에 대한 영향을 최소화하면서 기능을 추가하거나 변경할 수 있게 됩니다.
- 예를 들어 자동차에 옵션을 달 때, 다른 것은 그대로 두고 네비게이션 시스템을 추가하거나 엔진 성능을 개선할 수 있습니다.

## LSP: Liskov Substitution Principle
- 자식 객체는 언제나 부모 객체를 대체할 수 있어야 합니다.
- 즉, 두 객체가 항상 일반화/특수화 관계에 있어야 합니다.
- 리모컨으로 TV를 제어할 때 브랜드가 달라도 리모컨으로 TV를 켤 수 있습니다. TV가 삼성이든 LG든 리모컨은 동일한 역할을 수행합니다. 만약 어떤 브랜드의 TV는 리모컨으로 작동하고 다른 브랜드의 TV는 리모컨을 사용할 수 없다면 LSP 원칙을 위반한 사례가 됩니다. 자식 클래스(브랜드별 TV)가 부모 클래스(TV)의 기능을 동일하게 수행해야 합니다.

## ISP: Interface Segregation Principle
- 클라이언트는 자신이 사용하지 않는 인터페이스에 의존하지 않아야 합니다.
- 기능 단위로 인터페이스를 잘게 나누어 사용해야 합니다.
- 큰 인터페이스는 불필요한 의존성을 초래할 수 있습니다.
- 결합도가 높아지기 때문에 특정 기능의 변경이 여러 클래스에 영향을 미치게 됩니다.
- 다기능 프린터는 보통 프린트, 복사, 팩스, 스캔 기능을 가지고 있습니다. 만약 프린터 기능만 필요한데, 다른 복잡한 기능까지도 항상 사용해야 한다면 불편할 것입니다. 그래서 실제로는 각 기능을 독립적으로 선택해 사용할 수 있습니다.
  프린터만 사용하고 싶다면 프린트 인터페이스만 필요하고, 복사나 팩스 기능에 의존할 필요가 없습니다.

## DIP: Dependency Inversion Principle
- 구체적인 클래스에 의존하지 않고, 추상화된 인터페이스나 추상 클래스에 의존해야 합니다.
- 만약 구체적인 클래스에 의존한다면 구현 세부 사항이 변경되었을 때 시스템 구조에 영향을 끼치게 될 것입니다.
- 예를 들어 우리가 집에서 사용하는 전자기기는 콘센트만 꽂으면 사용할 수 있습니다. 전자기기는 콘센트가 제공하는 전기에 직접 의존하지 않고 플러그를 통해 전기를 공급받습니다. 콘센트(인터페이스)만 연결되어 있으면, 다양한 전자기기를 전기에 쉽게 연결할 수 있습니다.