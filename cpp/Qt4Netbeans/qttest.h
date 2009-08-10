#ifndef QTTEST_H
#define QTTEST_H

#include <QtGui/QWidget>
#include "ui_qttest.h"

class QtTest : public QWidget
{
    Q_OBJECT

public:
    QtTest(QWidget *parent = 0);
    ~QtTest();

private:
    Ui::QtTestClass ui;

private slots:
    void on_Button1_clicked();

};

#endif // QTTEST_H
