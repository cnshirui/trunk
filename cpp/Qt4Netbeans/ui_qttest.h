/********************************************************************************
** Form generated from reading ui file 'qttest.ui'
**
** Created: Mon Apr 6 13:11:52 2009
**      by: Qt User Interface Compiler version 4.4.3
**
** WARNING! All changes made in this file will be lost when recompiling ui file!
********************************************************************************/

#ifndef UI_QTTEST_H
#define UI_QTTEST_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QPushButton>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_QtTestClass
{
public:
    QPushButton *pushButton;
    QPushButton *pushButton_2;

    void setupUi(QWidget *QtTestClass)
    {
    if (QtTestClass->objectName().isEmpty())
        QtTestClass->setObjectName(QString::fromUtf8("QtTestClass"));
    QtTestClass->resize(400, 300);
    pushButton = new QPushButton(QtTestClass);
    pushButton->setObjectName(QString::fromUtf8("pushButton"));
    pushButton->setGeometry(QRect(50, 140, 75, 24));
    pushButton_2 = new QPushButton(QtTestClass);
    pushButton_2->setObjectName(QString::fromUtf8("pushButton_2"));
    pushButton_2->setGeometry(QRect(200, 140, 75, 24));

    retranslateUi(QtTestClass);
    QObject::connect(pushButton_2, SIGNAL(clicked()), QtTestClass, SLOT(close()));

    QMetaObject::connectSlotsByName(QtTestClass);
    } // setupUi

    void retranslateUi(QWidget *QtTestClass)
    {
    QtTestClass->setWindowTitle(QApplication::translate("QtTestClass", "QtTest", 0, QApplication::UnicodeUTF8));
    pushButton->setText(QApplication::translate("QtTestClass", "ShowText", 0, QApplication::UnicodeUTF8));
    pushButton_2->setText(QApplication::translate("QtTestClass", "Close", 0, QApplication::UnicodeUTF8));
    Q_UNUSED(QtTestClass);
    } // retranslateUi

};

namespace Ui {
    class QtTestClass: public Ui_QtTestClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_QTTEST_H
