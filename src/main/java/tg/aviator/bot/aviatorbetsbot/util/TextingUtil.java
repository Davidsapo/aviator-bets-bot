package tg.aviator.bot.aviatorbetsbot.util;

/**
 * Texting Util.
 *
 * @author David Sapozhnik
 */
public interface TextingUtil {

    // Error messages
    String NO_MESSAGE_ERROR = "Помилка. Пусте повідомлення‼️";

    // Commands
    String START_COMMAND = "/start";
    String BET_COMMAND_MEDIUM = "/bet_medium";
    String BET_COMMAND_HIGH = "/bet_high";
    String BET_COMMAND_BIG_WIN = "/bet_big_win";
    String DOUBLE_BET_COMMAND = "/double_bet";
    String BET_COMMAND_LOW = "/bet_low";
    String COEFFICIENTS_COMMAND = "/coefficients";
    String USERS_COMMAND = "/users";
    String USER_REQUESTS = "/user_requests";
    String USER_PROVIDE_ACCESS_COMMAND = "/user_provide_access";
    String USER_DENY_ACCESS_COMMAND = "/user_deny_access";
    String INIT_ADMIN_BUTTONS_COMMAND = "/init_admin_buttons";
    String STRATEGY_SELECTION_COMMAND = "/strategy_selection";
    String STRATEGY_INIT_COMMAND = "/strategy_init";
    String REQUEST_ACCESS_COMMAND = "/request_access";
    String PROCESS_PAYMENT_COMMAND = "/process_payment";
    String CHECK_ACCESS_COMMAND = "/check_access";

    // Messages
    String COMMAND_UNRECOGNIZED_MESSAGE = "Невідома команда‼️";
    String WELCOME_MESSAGE = """
            Вас вітає Sky Gambler - бот яким за допомогою складних алгоритмів та стратегій аналізує широко відому гру Aviator та вираховує коефіцієнти на наступний політ літака.
                        
            Бот пропонує на вибір декілька стратегій на вибір які різняться між собою рівнем ризику та величиною можливого виграшу.
                        
            Бот використовую алгоритми які були написані за допомогою штучного інтелеуту, що робить його унікальним і цій сфері.
                        
            У вас є можливість перевірити чесніть бота(звірити коефіцієнти в базі бота з коефіцієнтами на платформі) або зробити заявку на повну версію.
                        
            Рекомендовані птатформи для гри в Aviator:
              - Favbet
              - PinUp
            """;
    String PAYMENT_MESSAGE = """
            Доступ до боту коштує 200 грн.
                        
            Переказ здійснюється на монобанку після чого потрібно надіслати платіж на підтвердження.
                        
            Після підтвердження ви отримаєте повідомлення від боту та зможете користуватись всіма його можливостями.
                        
            Час підтвердження 5-15 хвилин у робочий час з 8:00 до 23:00
                        
            Посилання на банку:
                        
            https://send.monobank.ua/jar/4xKzufBwit
            """;
    String STRATEGY_SELECTION_MESSAGE = """
            Вибір стратегії
                        
            Без ризиків: Стабільна та перевірена стратегія без ризику але з невеликим коефіцієнтом 1.15х
                        
            Середній ризик - середній коефіцієнт: Стратегія з невеликим ризиком та більшим коефіцієнтом 2х.
                        
            Високий ризик - високий коефіцієнт: Стратегія з деякими ризиками але можливістю отримати коефіцієнт 10х.
                        
            Подвійна ставка: Це стратегія з малим ризиком і з двома одночасними ставками. Потребує більше уваги але дає можливість забрати гарантований коефіцієнт 1.5х та вискко ймовірний коефіцієнт 1.75х одночасно.
                        
            Великий виграш: Доволі ризикова стратегія яка повідомляє про період часу у який з’явиться  коефіцієнт 100х. Потрібно мати достатній баланс який дозволить програти декілька ставок але потім гарно відігратись за рахунок коефіцієнту 100х.
            """;
    String ACCESS_DENIED_MESSAGE = "Ваш запит відхилено. Будь ласка проведіть опиту та зробіть новий запит";
    String ACCESS_PROVIDED_MESSAGE = "Ваш запит підтверджено. Гарної гри!";
    String ACCESS_REQUIRED_MESSAGE = "У вас поки немає доступу до цього";
    String ACCESS_ALREADY_PROVIDED_MESSAGE = "Ви вже маєте доступ";
    String FORBIDDEN_MESSAGE = "Дія заборонена";
    String INTERNAL_ERROR_MESSAGE = "Щось пішло не так. Наша команда вже поацює над цим";
    String STRATEGY_SELECTED_MESSAGE = "Стратегія '%s' обрана. Гарної гри!";
    String PAYMENT_PROCESSING_MESSAGE = "Запит в процесі обробки. Вас буде повідомлено про результат";
    String NEW_REQUEST_MESSAGE = "New request from %s";
    String COEFFICIENTS_MESSAGE = """
            Істрія коефіцієнтів:
            %s
            Ви можете звірити їх з коефіцієнтами на ігрові платформі аби впевнетись в правильній роботі боту
            """;

}
