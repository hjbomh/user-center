-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '用户账号',
    avatarUrl    varchar(256)                       null comment '用户头像',
    gender       tinyint                            null comment '性别',
    UserPassword varchar(256)                       null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(256)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态   0表示正常  1表示不支持 ',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 null comment '是否删除     0表示正确     1表示删除',
    userRole     int      default 0                 null comment '用户角色  0表示普通权限 1 表示管理员',
    planetCode   varchar(256)                       null comment '星球编号'
)
    comment '用户表';

