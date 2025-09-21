// Роли пользователей
export const USER_ROLES = {
    ADMIN: 'ROLE_ADMIN',
    ASSOCIATION_ENG: 'ROLE_ASSOCIATION_ENG',
    ENTERPRISE_ENG: 'ROLE_ENTERPRISE_ENG',
    BRANCH_ENG: 'ROLE_BRANCH_ENG',
    USER: 'ROLE_USER'
};

// Отображаемые названия ролей
export const ROLE_DISPLAY_NAMES = {
    [USER_ROLES.ADMIN]: 'Администратор',
    [USER_ROLES.ASSOCIATION_ENG]: 'Инженер объединения',
    [USER_ROLES.ENTERPRISE_ENG]: 'Инженер предприятия',
    [USER_ROLES.BRANCH_ENG]: 'Инженер филиала',
    [USER_ROLES.USER]: 'Пользователь'
};

// Цвета для ролей
export const ROLE_COLORS = {
    [USER_ROLES.ADMIN]: 'red',
    [USER_ROLES.ASSOCIATION_ENG]: 'blue',
    [USER_ROLES.ENTERPRISE_ENG]: 'green',
    [USER_ROLES.BRANCH_ENG]: 'orange',
    [USER_ROLES.USER]: 'gray'
};

// Статусы планов
export const PLAN_STATUS = {
    DRAFT: 'DRAFT',
    SENT: 'SENT',
    APPROVED: 'APPROVED'
};

// Отображаемые названия статусов планов
export const PLAN_STATUS_DISPLAY = {
    [PLAN_STATUS.DRAFT]: 'Черновик',
    [PLAN_STATUS.SENT]: 'Отправлен',
    [PLAN_STATUS.APPROVED]: 'Утвержден'
};

// Цвета статусов планов
export const PLAN_STATUS_COLORS = {
    [PLAN_STATUS.DRAFT]: 'blue',
    [PLAN_STATUS.SENT]: 'orange',
    [PLAN_STATUS.APPROVED]: 'green'
};

// Статусы отчетов
export const REPORT_STATUS = {
    DRAFT: 'DRAFT',
    SENT: 'SENT'
};

// Отображаемые названия статусов отчетов
export const REPORT_STATUS_DISPLAY = {
    [REPORT_STATUS.DRAFT]: 'Черновик',
    [REPORT_STATUS.SENT]: 'Отправлен'
};

// Цвета статусов отчетов
export const REPORT_STATUS_COLORS = {
    [REPORT_STATUS.DRAFT]: 'blue',
    [REPORT_STATUS.SENT]: 'green'
};

// Типы сетей
export const NETWORK_TYPES = {
    MAIN: 'MAIN',
    DISTRIBUTION: 'DISTRIBUTION'
};

// Отображаемые названия типов сетей
export const NETWORK_TYPE_DISPLAY = {
    [NETWORK_TYPES.MAIN]: 'Основная сеть',
    [NETWORK_TYPES.DISTRIBUTION]: 'Распределительная сеть'
};

// Цвета типов сетей
export const NETWORK_TYPE_COLORS = {
    [NETWORK_TYPES.MAIN]: 'blue',
    [NETWORK_TYPES.DISTRIBUTION]: 'green'
};

// Кварталы
export const QUARTERS = {
    1: 'Первый квартал',
    2: 'Второй квартал',
    3: 'Третий квартал',
    4: 'Четвертый квартал'
};

// Месяцы
export const MONTHS = {
    1: 'Январь',
    2: 'Февраль',
    3: 'Март',
    4: 'Апрель',
    5: 'Май',
    6: 'Июнь',
    7: 'Июль',
    8: 'Август',
    9: 'Сентябрь',
    10: 'Октябрь',
    11: 'Ноябрь',
    12: 'Декабрь'
};

// API endpoints
export const API_ENDPOINTS = {
    AUTH: {
        LOGIN: '/auth/signin',
        REGISTER: '/auth/signup'
    },
    USERS: '/admin/users',
    PLANS: '/plans',
    REPORTS: '/reports',
    ASSOCIATIONS: '/associations',
    ENTERPRISES: '/enterprises',
    BRANCHES: '/branches',
    WORK_CATEGORIES: '/work-categories'
};

// Локальное хранилище
export const STORAGE_KEYS = {
    TOKEN: 'token',
    USER: 'user'
};

// Сообщения об ошибках
export const ERROR_MESSAGES = {
    NETWORK_ERROR: 'Ошибка сети. Проверьте подключение к интернету.',
    UNAUTHORIZED: 'Необходима авторизация.',
    FORBIDDEN: 'Доступ запрещен.',
    NOT_FOUND: 'Ресурс не найден.',
    SERVER_ERROR: 'Ошибка сервера.',
    VALIDATION_ERROR: 'Ошибка валидации данных.',
    UNKNOWN_ERROR: 'Неизвестная ошибка.'
};

// Сообщения об успехе
export const SUCCESS_MESSAGES = {
    LOGIN: 'Вход выполнен успешно!',
    LOGOUT: 'Выход выполнен успешно!',
    CREATE: 'Запись успешно создана!',
    UPDATE: 'Запись успешно обновлена!',
    DELETE: 'Запись успешно удалена!'
};

// Валидационные правила
export const VALIDATION_RULES = {
    USERNAME: {
        MIN_LENGTH: 3,
        MAX_LENGTH: 20,
        PATTERN: /^[a-zA-Z0-9_]+$/
    },
    PASSWORD: {
        MIN_LENGTH: 6,
        MAX_LENGTH: 40
    },
    NAME: {
        MAX_LENGTH: 255
    },
    WORK_NAME: {
        MAX_LENGTH: 500
    }
};

// Настройки приложения
export const APP_CONFIG = {
    DEFAULT_PAGE_SIZE: 10,
    MAX_PAGE_SIZE: 100,
    DEBOUNCE_TIME: 300,
    TOKEN_EXPIRY_CHECK: 5 * 60 * 1000 // 5 минут
};

// Форматы дат
export const DATE_FORMATS = {
    DISPLAY: 'DD.MM.YYYY',
    API: 'YYYY-MM-DD',
    MONTH_YEAR: 'MMMM YYYY',
    YEAR: 'YYYY'
};

// Цветовая палитра приложения
export const COLORS = {
    PRIMARY: '#1890ff',
    SUCCESS: '#52c41a',
    WARNING: '#faad14',
    ERROR: '#f5222d',
    INFO: '#1890ff',
    BACKGROUND: '#f5f5f5',
    BORDER: '#d9d9d9'
};

// Разрешения для ролей
export const PERMISSIONS = {
    [USER_ROLES.ADMIN]: [
        'manage_users',
        'manage_associations',
        'manage_enterprises',
        'manage_branches',
        'manage_work_categories',
        'view_all_plans',
        'view_all_reports'
    ],
    [USER_ROLES.ASSOCIATION_ENG]: [
        'manage_enterprises',
        'manage_branches',
        'manage_work_categories',
        'view_association_plans',
        'view_association_reports',
        'approve_plans'
    ],
    [USER_ROLES.ENTERPRISE_ENG]: [
        'manage_branches',
        'view_enterprise_plans',
        'view_enterprise_reports',
        'review_plans'
    ],
    [USER_ROLES.BRANCH_ENG]: [
        'create_plans',
        'edit_own_plans',
        'create_reports',
        'edit_own_reports',
        'view_own_plans',
        'view_own_reports'
    ],
    [USER_ROLES.USER]: [
        'view_own_plans',
        'view_own_reports'
    ]
};

// Уровни доступа
export const ACCESS_LEVELS = {
    NONE: 0,
    VIEW: 1,
    CREATE: 2,
    EDIT: 3,
    DELETE: 4,
    MANAGE: 5
};

// Экспорт всех констант по умолчанию
export default {
    USER_ROLES,
    ROLE_DISPLAY_NAMES,
    ROLE_COLORS,
    PLAN_STATUS,
    PLAN_STATUS_DISPLAY,
    PLAN_STATUS_COLORS,
    REPORT_STATUS,
    REPORT_STATUS_DISPLAY,
    REPORT_STATUS_COLORS,
    NETWORK_TYPES,
    NETWORK_TYPE_DISPLAY,
    NETWORK_TYPE_COLORS,
    QUARTERS,
    MONTHS,
    API_ENDPOINTS,
    STORAGE_KEYS,
    ERROR_MESSAGES,
    SUCCESS_MESSAGES,
    VALIDATION_RULES,
    APP_CONFIG,
    DATE_FORMATS,
    COLORS,
    PERMISSIONS,
    ACCESS_LEVELS
};
